package services

import cart.*
import commands.AddItemToCart
import commands.RemoveItemCommand
import events.*
import eventstore.EventStore

interface CartService {
    fun addItemToCart(command: AddItemToCart): Result<AddItemResult>
    fun removeItem(command: RemoveItemCommand): Result<RemoveItemResult>
}

data class RemoveItemResult(
    val cart: Cart,
    val events: List<DomainEvent>
)

data class AddItemResult(
    val cart: Cart,
    val events: List<DomainEvent>
)

class CartServiceImpl(
    private val eventStore: EventStore
) : CartService {
    
    override fun addItemToCart(command: AddItemToCart): Result<AddItemResult> {
        return try {
            val cart = if (command.cartId != null) {
                loadCart(command.cartId) ?: return Result.failure(
                    IllegalArgumentException("Cart ${command.cartId.value} not found")
                )
            } else {
                // Create new cart
                Cart(
                    cartId = CartId.generate(),
                    lineItems = emptyMap(),
                    version = EventVersion.initial
                )
            }
            
            // Check capacity constraints
            if (!cart.canAddItem(command.quantity)) {
                return Result.failure(
                    IllegalArgumentException("A cart cannot hold more than three (3) items.")
                )
            }
            
            val events = mutableListOf<DomainEvent>()
            
            // Add cart creation event if new cart
            if (command.cartId == null) {
                events.add(CartCreated(cart.cartId))
            }
            
            // Calculate new state
            val existingLineItem = cart.lineItems[command.productId]
            val newQuantity = if (existingLineItem != null) {
                existingLineItem.quantity + command.quantity
            } else {
                command.quantity
            }
            val newTotalQuantity = cart.totalQuantity - (existingLineItem?.quantity ?: 0) + newQuantity
            
            // Add item event
            events.add(ItemAddedToCart(
                cartId = cart.cartId,
                productId = command.productId,
                quantity = command.quantity,
                newTotalQuantity = newTotalQuantity
            ))
            
            // Persist events
            val appendResult = eventStore.append(cart.cartId.value, cart.version, events)
            if (appendResult.isFailure) {
                return Result.failure(appendResult.exceptionOrNull()!!)
            }
            
            // Build updated cart state
            val newLineItem = LineItem(command.productId, newQuantity)
            val updatedCart = cart.copy(
                lineItems = cart.lineItems + (command.productId to newLineItem),
                version = EventVersion(cart.version.value + events.size)
            )
            
            Result.success(AddItemResult(updatedCart, events))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun loadCart(cartId: CartId): Cart? {
        val events = eventStore.getEvents(cartId.value)
        if (events.isEmpty()) return null
        
        var cart = Cart(cartId, emptyMap(), EventVersion.initial)
        var version = EventVersion.initial
        
        events.forEach { event ->
            version = version.increment()
            when (event) {
                is CartCreated -> {
                    // Cart already initialized
                }
                is ItemAddedToCart -> {
                    val existingItem = cart.lineItems[event.productId]
                    val newQuantity = if (existingItem != null) {
                        existingItem.quantity + event.quantity
                    } else {
                        event.quantity
                    }
                    val newLineItem = LineItem(event.productId, newQuantity)
                    cart = cart.copy(
                        lineItems = cart.lineItems + (event.productId to newLineItem),
                        version = version
                    )
                }
                is ItemRemovedEvent -> {
                    val existingItem = cart.lineItems[event.productId]
                    if (existingItem != null) {
                        if (event.remainingQuantity > 0) {
                            val updatedItem = existingItem.copy(quantity = event.remainingQuantity)
                            cart = cart.copy(
                                lineItems = cart.lineItems + (event.productId to updatedItem),
                                version = version
                            )
                        } else {
                            // Remove item completely
                            cart = cart.copy(
                                lineItems = cart.lineItems - event.productId,
                                version = version
                            )
                        }
                    }
                }
            }
        }
        
        return cart
    }
    
    override fun removeItem(command: RemoveItemCommand): Result<RemoveItemResult> {
        return try {
            val cart = loadCart(command.cartId) ?: return Result.failure(
                IllegalArgumentException("Cart ${command.cartId.value} not found")
            )
            
            // Check version for concurrency control
            if (cart.version != command.expectedVersion) {
                return Result.failure(ConcurrencyException(
                    command.cartId,
                    command.expectedVersion,
                    cart.version
                ))
            }
            
            // Use Cart domain method to perform removal
            val updatedCart = try {
                cart.removeItem(command.productId, 1)
            } catch (e: Exception) {
                return Result.failure(e)
            }
            
            // Create domain event for the removal
            val existingItem = cart.lineItems[command.productId]!!
            val remainingQuantity = if (existingItem.quantity > 1) existingItem.quantity - 1 else 0
            
            val event = ItemRemovedEvent(
                cartId = command.cartId,
                productId = command.productId,
                quantity = 1,
                remainingQuantity = remainingQuantity,
                version = updatedCart.version
            )
            
            // Persist event
            val appendResult = eventStore.append(cart.cartId.value, cart.version, listOf(event))
            if (appendResult.isFailure) {
                return Result.failure(appendResult.exceptionOrNull()!!)
            }
            
            Result.success(RemoveItemResult(updatedCart, listOf(event)))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
