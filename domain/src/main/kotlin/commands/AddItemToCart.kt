package commands

import cart.CartId
import cart.ProductId

data class AddItemToCart(
    val cartId: CartId?,  // null when creating new cart
    val productId: ProductId,
    val quantity: Int = 1
) {
    init {
        require(quantity > 0) { "Quantity must be positive" }
    }
}
