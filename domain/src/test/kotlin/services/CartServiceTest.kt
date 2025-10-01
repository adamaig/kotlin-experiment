package services

import cart.*
import commands.RemoveItemCommand
import events.EventVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CartServiceTest {

    @Test
    fun `should remove item successfully with valid command`() {
        // Given
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")
        val expectedVersion = EventVersion(5)
        val command = RemoveItemCommand(cartId, productId, expectedVersion)

        // When & Then
        // This test will fail until CartService.removeItem() is implemented
        // Testing the contract defined in the API specification
        assertFailsWith<NotImplementedError> {
            // val cartService = CartService() // Will be implemented later
            // cartService.removeItem(command)
            throw NotImplementedError("CartService.removeItem() not yet implemented")
        }
    }

    @Test
    fun `should throw ItemNotInCartException when product not in cart`() {
        // Given
        val cartId = CartId("cart-456")
        val productId = ProductId("MISSING-PROD")
        val expectedVersion = EventVersion(3)
        val command = RemoveItemCommand(cartId, productId, expectedVersion)

        // When & Then
        assertFailsWith<NotImplementedError> {
            // This will be replaced with actual ItemNotInCartException test once implemented
            throw NotImplementedError("CartService.removeItem() not yet implemented")
        }
    }

    @Test
    fun `should throw EmptyCartException when cart is empty`() {
        // Given
        val cartId = CartId("empty-cart")
        val productId = ProductId("ANY-PROD")
        val expectedVersion = EventVersion.initial
        val command = RemoveItemCommand(cartId, productId, expectedVersion)

        // When & Then
        assertFailsWith<NotImplementedError> {
            // This will be replaced with actual EmptyCartException test once implemented
            throw NotImplementedError("CartService.removeItem() not yet implemented")
        }
    }

    @Test
    fun `should throw ConcurrencyException on version mismatch`() {
        // Given
        val cartId = CartId("cart-789")
        val productId = ProductId("PROD-X")
        val wrongVersion = EventVersion(999)
        val command = RemoveItemCommand(cartId, productId, wrongVersion)

        // When & Then
        assertFailsWith<NotImplementedError> {
            // This will be replaced with actual ConcurrencyException test once implemented
            throw NotImplementedError("CartService.removeItem() not yet implemented")
        }
    }

    @Test
    fun `should return updated cart after successful removal`() {
        // Given
        val cartId = CartId("cart-return-test")
        val productId = ProductId("PROD-RETURN")
        val expectedVersion = EventVersion(2)
        val command = RemoveItemCommand(cartId, productId, expectedVersion)

        // When & Then
        assertFailsWith<NotImplementedError> {
            // This will verify the contract that service returns updated Cart
            throw NotImplementedError("CartService.removeItem() not yet implemented")
        }
    }

    @Test
    fun `should increment cart version after successful removal`() {
        // Given
        val cartId = CartId("version-test-cart")
        val productId = ProductId("VERSION-PROD")
        val expectedVersion = EventVersion(7)
        val command = RemoveItemCommand(cartId, productId, expectedVersion)

        // When & Then
        assertFailsWith<NotImplementedError> {
            // This will verify version increment behavior
            throw NotImplementedError("CartService.removeItem() not yet implemented")
        }
    }
}
