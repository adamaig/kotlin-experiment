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
    
    /**
     * Removes an item from the cart.
     * 
     * @param productId The product to remove
     * @param quantityToRemove The quantity to remove (defaults to all)
     * @return A new Cart instance with the item removed
     * @throws EmptyCartException if the cart is empty
     * @throws ItemNotInCartException if the product is not in the cart
     */
    fun removeItem(productId: ProductId, quantityToRemove: Int? = null): Cart {
        // Business rule: Cannot remove from empty cart
        if (lineItems.isEmpty()) {
            throw EmptyCartException(cartId)
        }
        
        // Business rule: Product must exist in cart
        val existingItem = lineItems[productId]
            ?: throw ItemNotInCartException(cartId, productId)
        
        val actualQuantityToRemove = quantityToRemove ?: existingItem.quantity
        
        // Validate removal quantity
        require(actualQuantityToRemove > 0) { "Quantity to remove must be positive" }
        require(actualQuantityToRemove <= existingItem.quantity) { 
            "Cannot remove more items than exist in cart" 
        }
        
        val newLineItems = if (actualQuantityToRemove >= existingItem.quantity) {
            // Remove the item completely
            lineItems - productId
        } else {
            // Reduce the quantity
            val updatedItem = existingItem.copy(quantity = existingItem.quantity - actualQuantityToRemove)
            lineItems + (productId to updatedItem)
        }
        
        return copy(
            lineItems = newLineItems,
            version = version.increment()
        )
    }
}
