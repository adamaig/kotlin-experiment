package events

import cart.CartId
import cart.ProductId

/**
 * Domain event representing the removal of an item from a cart.
 * 
 * This event is immutable and represents a fact that has occurred in the domain.
 * It follows the event sourcing pattern where all state changes are captured as events.
 * 
 * @property cartId The unique identifier of the cart from which the item was removed
 * @property productId The unique identifier of the product that was removed
 * @property quantity The quantity that was removed (must be positive)
 * @property remainingQuantity The quantity remaining in cart after removal (must be non-negative)
 * @property version The new version of the cart after this event
 * @property timestamp When the event occurred
 */
data class ItemRemovedEvent(
    val cartId: CartId,
    val productId: ProductId,
    val quantity: Int,
    val remainingQuantity: Int,
    val version: EventVersion,
    override val timestamp: java.time.Instant = java.time.Instant.now()
) : DomainEvent {
    
    init {
        // Validate business rules for item removal
        require(quantity > 0) { "Removed quantity must be positive" }
        require(remainingQuantity >= 0) { "Remaining quantity cannot be negative" }
        require(productId.value.isNotBlank()) { "Product ID cannot be blank" }
        require(version.value > 0L) { "Event version must be positive" }
        
        // Business rule: If remaining quantity is 0, the item should be completely removed
        // This is enforced by the Cart aggregate, but we validate consistency here
    }
    
    /**
     * Creates a new ItemRemovedEvent with validated business rules.
     * 
     * @param cartId The cart from which item was removed
     * @param productId The product that was removed
     * @param removedQuantity How much was removed
     * @param quantityAfterRemoval How much remains in cart
     * @param newVersion The new cart version
     * @return A validated ItemRemovedEvent
     */
    companion object {
        fun create(
            cartId: CartId,
            productId: ProductId,
            removedQuantity: Int,
            quantityAfterRemoval: Int,
            newVersion: EventVersion
        ): ItemRemovedEvent {
            return ItemRemovedEvent(
                cartId = cartId,
                productId = productId,
                quantity = removedQuantity,
                remainingQuantity = quantityAfterRemoval,
                version = newVersion
            )
        }
    }
}
