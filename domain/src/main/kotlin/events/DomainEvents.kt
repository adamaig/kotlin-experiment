package events

import cart.CartId
import cart.ProductId
import java.time.Instant

interface DomainEvent {
    val timestamp: Instant
}

data class CartCreated(
    val cartId: CartId,
    override val timestamp: Instant = Instant.now()
) : DomainEvent

data class ItemAddedToCart(
    val cartId: CartId,
    val productId: ProductId,
    val quantity: Int,
    val newTotalQuantity: Int,
    override val timestamp: Instant = Instant.now()
) : DomainEvent {
    init {
        require(quantity > 0) { "Added quantity must be positive" }
        require(newTotalQuantity <= 3) { "Total quantity cannot exceed capacity" }
    }
}
