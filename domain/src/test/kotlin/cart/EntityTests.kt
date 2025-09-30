package cart

import events.EventVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LineItemTest {

    @Test
    fun `should create LineItem with valid data`() {
        val productId = ProductId("prod-123")
        val lineItem = LineItem(productId, 2)
        
        assertEquals(productId, lineItem.productId)
        assertEquals(2, lineItem.quantity)
    }

    @Test
    fun `should reject zero quantity`() {
        assertFailsWith<IllegalArgumentException> {
            LineItem(ProductId("prod-123"), 0)
        }
    }

    @Test
    fun `should reject negative quantity`() {
        assertFailsWith<IllegalArgumentException> {
            LineItem(ProductId("prod-123"), -1)
        }
    }

    @Test
    fun `should add quantity correctly`() {
        val lineItem = LineItem(ProductId("prod-123"), 2)
        val updated = lineItem.addQuantity(3)
        
        assertEquals(5, updated.quantity)
        assertEquals(lineItem.productId, updated.productId)
    }

    @Test
    fun `should be equal when productId and quantity are equal`() {
        val lineItem1 = LineItem(ProductId("prod-123"), 2)
        val lineItem2 = LineItem(ProductId("prod-123"), 2)
        assertEquals(lineItem1, lineItem2)
    }
}

class CartTest {

    private fun createEmptyCart(): Cart {
        return Cart(
            cartId = CartId("cart-123"),
            lineItems = emptyMap(),
            version = EventVersion.initial
        )
    }

    private fun createCartWithItem(productId: ProductId, quantity: Int): Cart {
        val lineItem = LineItem(productId, quantity)
        return Cart(
            cartId = CartId("cart-123"),
            lineItems = mapOf(productId to lineItem),
            version = EventVersion(1)
        )
    }

    @Test
    fun `should create empty cart`() {
        val cart = createEmptyCart()
        
        assertEquals(0, cart.totalQuantity)
        assertTrue(cart.lineItems.isEmpty())
        assertFalse(cart.isAtCapacity)
    }

    @Test
    fun `should calculate total quantity correctly`() {
        val cart = Cart(
            cartId = CartId("cart-123"),
            lineItems = mapOf(
                ProductId("prod-1") to LineItem(ProductId("prod-1"), 2),
                ProductId("prod-2") to LineItem(ProductId("prod-2"), 1)
            ),
            version = EventVersion(2)
        )
        
        assertEquals(3, cart.totalQuantity)
    }

    @Test
    fun `should be at capacity when total quantity is 3`() {
        val cart = Cart(
            cartId = CartId("cart-123"),
            lineItems = mapOf(
                ProductId("prod-1") to LineItem(ProductId("prod-1"), 2),
                ProductId("prod-2") to LineItem(ProductId("prod-2"), 1)
            ),
            version = EventVersion(2)
        )
        
        assertTrue(cart.isAtCapacity)
    }

    @Test
    fun `should check if can add item within capacity`() {
        val cart = createCartWithItem(ProductId("prod-1"), 2)
        
        assertTrue(cart.canAddItem(1))
        assertFalse(cart.canAddItem(2))
    }

    @Test
    fun `should check capacity for empty cart`() {
        val cart = createEmptyCart()
        
        assertTrue(cart.canAddItem(1))
        assertTrue(cart.canAddItem(2))
        assertTrue(cart.canAddItem(3))
        assertFalse(cart.canAddItem(4))
    }
}
