package cart

/**
 * Exception thrown when attempting to remove an item that is not present in the cart.
 * 
 * This represents a business rule violation where the user is trying to remove
 * a product that doesn't exist in their cart.
 * 
 * @property cartId The ID of the cart that was accessed
 * @property productId The ID of the product that was not found
 */
class ItemNotInCartException(
    val cartId: CartId,
    val productId: ProductId
) : DomainException("Product ${productId.value} is not in cart ${cartId.value}") {
    
    /**
     * Alternative constructor for cases where only the product ID is relevant.
     */
    constructor(productId: ProductId) : this(
        CartId("unknown"), 
        productId
    )
    
    /**
     * Provides a user-friendly error message for API responses.
     */
    fun toUserMessage(): String {
        return "The requested item is not in your cart."
    }
    
    /**
     * Provides detailed error information for debugging.
     */
    fun toDetailedMessage(): String {
        return "Item removal failed: Product '${productId.value}' not found in cart '${cartId.value}'"
    }
}
