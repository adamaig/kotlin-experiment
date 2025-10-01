package cart

import events.EventVersion

/**
 * Exception thrown when a concurrency conflict occurs during cart modification.
 * 
 * This represents a violation of optimistic concurrency control where the
 * expected version doesn't match the actual current version of the cart.
 * 
 * @property cartId The ID of the cart that had the version conflict
 * @property expectedVersion The version that was expected
 * @property actualVersion The actual current version of the cart
 */
class ConcurrencyException(
    val cartId: CartId,
    val expectedVersion: EventVersion,
    val actualVersion: EventVersion
) : DomainException(
    "Concurrency conflict for cart ${cartId.value}: expected version ${expectedVersion.value}, " +
    "but actual version is ${actualVersion.value}"
) {
    
    /**
     * Provides a user-friendly error message for API responses.
     */
    fun toUserMessage(): String {
        return "The cart has been modified by another operation. Please refresh and try again."
    }
    
    /**
     * Provides detailed error information for debugging.
     */
    fun toDetailedMessage(): String {
        return "Concurrency conflict: Cart '${cartId.value}' expected version ${expectedVersion.value} " +
               "but found version ${actualVersion.value}"
    }
    
    /**
     * Calculates how far behind the expected version was.
     */
    fun getVersionDifference(): Long {
        return actualVersion.value - expectedVersion.value
    }
}
