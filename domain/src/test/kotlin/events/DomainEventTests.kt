package events

import cart.CartId
import cart.ProductId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DomainEventTest {

    @Test
    fun `CartCreated should have required properties`() {
        val cartId = CartId("cart-123")
        val event = CartCreated(cartId)
        
        assertEquals(cartId, event.cartId)
        assertTrue(event.timestamp != null)
    }

    @Test 
    fun `ItemAddedToCart should have required properties`() {
        val cartId = CartId("cart-123")
        val productId = ProductId("prod-456")
        val event = ItemAddedToCart(
            cartId = cartId,
            productId = productId,
            quantity = 2,
            newTotalQuantity = 2
        )
        
        assertEquals(cartId, event.cartId)
        assertEquals(productId, event.productId)
        assertEquals(2, event.quantity)
        assertEquals(2, event.newTotalQuantity)
        assertTrue(event.timestamp != null)
    }

    @Test
    fun `ItemAddedToCart should reject zero quantity`() {
        assertFailsWith<IllegalArgumentException> {
            ItemAddedToCart(
                cartId = CartId("cart-123"),
                productId = ProductId("prod-456"),
                quantity = 0,
                newTotalQuantity = 0
            )
        }
    }

    @Test
    fun `ItemAddedToCart should reject negative quantity`() {
        assertFailsWith<IllegalArgumentException> {
            ItemAddedToCart(
                cartId = CartId("cart-123"),
                productId = ProductId("prod-456"),
                quantity = -1,
                newTotalQuantity = -1
            )
        }
    }

    @Test
    fun `ItemAddedToCart should reject total quantity exceeding capacity`() {
        assertFailsWith<IllegalArgumentException> {
            ItemAddedToCart(
                cartId = CartId("cart-123"),
                productId = ProductId("prod-456"),
                quantity = 1,
                newTotalQuantity = 4  // Exceeds capacity of 3
            )
        }
    }
}
