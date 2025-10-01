package commands

import cart.CartId
import cart.ProductId
import events.EventVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RemoveItemCommandTest {

    @Test
    fun `should create valid RemoveItemCommand with valid parameters`() {
        // Given
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")
        val version = EventVersion(5)

        // When
        val command = RemoveItemCommand(cartId, productId, version)

        // Then
        assertEquals(cartId, command.cartId)
        assertEquals(productId, command.productId)
        assertEquals(version, command.expectedVersion)
    }

    @Test
    fun `should validate cartId is not empty`() {
        // Given - This will fail at CartId creation, not RemoveItemCommand creation
        val productId = ProductId("PROD-A")
        val version = EventVersion(1)

        // When & Then
        assertFailsWith<IllegalArgumentException> {
            val emptyCartId = CartId("")  // This is where the exception is thrown
            RemoveItemCommand(emptyCartId, productId, version)
        }
    }

    @Test
    fun `should validate productId is not empty`() {
        // Given - This will fail at ProductId creation
        val cartId = CartId("cart-123")
        val version = EventVersion(1)

        // When & Then
        assertFailsWith<IllegalArgumentException> {
            val emptyProductId = ProductId("")  // This is where the exception is thrown
            RemoveItemCommand(cartId, emptyProductId, version)
        }
    }

    @Test
    fun `should validate version is non-negative`() {
        // Given - This will fail at EventVersion creation
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")

        // When & Then
        assertFailsWith<IllegalArgumentException> {
            val negativeVersion = EventVersion(-1)  // This is where the exception is thrown
            RemoveItemCommand(cartId, productId, negativeVersion)
        }
    }

    @Test
    fun `should allow zero version for new carts`() {
        // Given
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")
        val zeroVersion = EventVersion(0)

        // When
        val command = RemoveItemCommand(cartId, productId, zeroVersion)

        // Then
        assertEquals(zeroVersion, command.expectedVersion)
    }
}
