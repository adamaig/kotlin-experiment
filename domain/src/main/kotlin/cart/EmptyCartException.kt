package cart

/**
 * Exception thrown when attempting to remove an item from an empty cart.
 * 
 * This represents a business rule violation where the user is trying to remove
 * any item from a cart that contains no items.
 * 
 * @property cartId The ID of the empty cart that was accessed
 */
class EmptyCartException(
    val cartId: CartId
) : DomainException("Cannot remove items from empty cart ${cartId.value}") {
    
    /**
     * Alternative constructor for cases where cart ID is not available.
     */
    constructor() : this(CartId("unknown"))
    
    /**
     * Provides a user-friendly error message for API responses.
     */
    fun toUserMessage(): String {
        return "Your cart is empty. There are no items to remove."
    }
    
    /**
     * Provides detailed error information for debugging.
     */
    fun toDetailedMessage(): String {
        return "Item removal failed: Cart '${cartId.value}' is empty"
    }
    
    /**
     * Checks if this exception applies to a specific cart.
     */
    fun appliesToCart(checkCartId: CartId): Boolean {
        return cartId == checkCartId
    }
}
