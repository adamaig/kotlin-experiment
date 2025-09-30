package services

import cart.*
import commands.AddItemToCart
import events.*
import eventstore.EventStore

interface CartService {
    fun addItemToCart(command: AddItemToCart): Result<AddItemResult>
}

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
            }
        }
        
        return cart
    }
}
