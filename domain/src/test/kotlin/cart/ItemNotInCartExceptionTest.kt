package cart

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ItemNotInCartExceptionTest {

    @Test
    fun `should create exception with cart and product IDs`() {
        // Given
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")

        // When
        val exception = ItemNotInCartException(cartId, productId)

        // Then
        assertEquals(cartId, exception.cartId)
        assertEquals(productId, exception.productId)
        assertTrue(exception.message!!.contains("PROD-A"))
        assertTrue(exception.message!!.contains("cart-123"))
    }

    @Test
    fun `should have descriptive error message`() {
        // Given
        val cartId = CartId("test-cart")
        val productId = ProductId("TEST-PRODUCT")

        // When
        val exception = ItemNotInCartException(cartId, productId)

        // Then
        val expectedMessage = "Product TEST-PRODUCT is not in cart test-cart"
        assertEquals(expectedMessage, exception.message)
    }

    @Test
    fun `should be a domain exception`() {
        // Given
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")

        // When
        val exception = ItemNotInCartException(cartId, productId)

        // Then
        assertTrue(exception is DomainException)
    }

    @Test
    fun `should preserve cart and product information for error handling`() {
        // Given
        val cartId = CartId("order-cart-456")
        val productId = ProductId("SPECIAL-ITEM-789")

        // When
        val exception = ItemNotInCartException(cartId, productId)

        // Then
        assertEquals("order-cart-456", exception.cartId.value)
        assertEquals("SPECIAL-ITEM-789", exception.productId.value)
    }
}
