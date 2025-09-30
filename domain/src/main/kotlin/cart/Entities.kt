package cart

import events.EventVersion

data class LineItem(
    val productId: ProductId,
    val quantity: Int
) {
    init {
        require(quantity > 0) { "Quantity must be positive" }
    }
    
    fun addQuantity(additionalQuantity: Int): LineItem =
        copy(quantity = quantity + additionalQuantity)
}

data class Cart(
    val cartId: CartId,
    val lineItems: Map<ProductId, LineItem>,
    val version: EventVersion
) {
    val totalQuantity: Int get() = lineItems.values.sumOf { it.quantity }
    val isAtCapacity: Boolean get() = totalQuantity >= 3
    
    fun canAddItem(quantity: Int): Boolean = 
        totalQuantity + quantity <= 3
}
