package commands

import cart.CartId
import cart.ProductId
import events.EventVersion

/**
 * Command to remove an item from a cart.
 * 
 * This command follows the domain-driven design pattern and encapsulates
 * the intent to remove a specific product from a cart at a specific version.
 * 
 * @property cartId The unique identifier of the cart
 * @property productId The unique identifier of the product to remove
 * @property expectedVersion The expected version for optimistic concurrency control
 */
data class RemoveItemCommand(
    val cartId: CartId,
    val productId: ProductId,
    val expectedVersion: EventVersion
)
