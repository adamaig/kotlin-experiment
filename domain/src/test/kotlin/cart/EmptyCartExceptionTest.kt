package cart

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmptyCartExceptionTest {

    @Test
    fun `should create exception with cart ID`() {
        // Given
        val cartId = CartId("empty-cart-123")

        // When
        val exception = EmptyCartException(cartId)

        // Then
        assertEquals(cartId, exception.cartId)
        assertTrue(exception.message!!.contains("empty-cart-123"))
    }

    @Test
    fun `should have descriptive error message`() {
        // Given
        val cartId = CartId("test-empty-cart")

        // When
        val exception = EmptyCartException(cartId)

        // Then
        val expectedMessage = "Cannot remove items from empty cart test-empty-cart"
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun `should be a domain exception`() {
        // Given
        val cartId = CartId("cart-123")

        // When
        val exception = EmptyCartException(cartId)

        // Then
        assertTrue(exception is DomainException)
    }

    @Test
    fun `should preserve cart ID for error handling`() {
        // Given
        val cartId = CartId("business-cart-789")

        // When
        val exception = EmptyCartException(cartId)

        // Then
        assertEquals("business-cart-789", exception.cartId.value)
    }

    @Test
    fun `should clearly indicate the operation that failed`() {
        // Given
        val cartId = CartId("my-cart")

        // When
        val exception = EmptyCartException(cartId)

        // Then
        assertTrue(exception.message!!.contains("Cannot remove items"))
        assertTrue(exception.message!!.contains("empty cart"))
    }
}
