package events

import cart.CartId
import cart.ProductId
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Tests for ItemRemovedEvent domain event (T004)
 * Following TDD Red phase - these tests will fail until implementation exists
 */
class ItemRemovedEventTest {

    @Test
    fun `should create ItemRemovedEvent with valid parameters`() {
        // Given
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")
        val quantityRemoved = 1
        val remainingQuantity = 2
        val version = EventVersion(5)
        val beforeTime = Instant.now()

        // When
        val event = ItemRemovedEvent(cartId, productId, quantityRemoved, remainingQuantity, version)

        // Then
        assertEquals(cartId, event.cartId)
        assertEquals(productId, event.productId)
        assertEquals(quantityRemoved, event.quantity)
        assertEquals(remainingQuantity, event.remainingQuantity)
        assertEquals(version, event.version)
        assertTrue(event.timestamp.isAfter(beforeTime) || event.timestamp == beforeTime)
    }

    @Test
    fun `should create ItemRemovedEvent with custom timestamp`() {
        // Given
        val cartId = CartId("cart-123")
        val productId = ProductId("PROD-A")
        val quantityRemoved = 2
        val remainingQuantity = 0
        val version = EventVersion(6)
        val customTimestamp = Instant.now().minusSeconds(3600)

        // When
        val event = ItemRemovedEvent(cartId, productId, quantityRemoved, remainingQuantity, version, customTimestamp)

        // Then
        assertEquals(cartId, event.cartId)
        assertEquals(productId, event.productId)
        assertEquals(quantityRemoved, event.quantity)
        assertEquals(remainingQuantity, event.remainingQuantity)
        assertEquals(version, event.version)
        assertEquals(customTimestamp, event.timestamp)
    }

    @Test
    fun `should reject ItemRemovedEvent with invalid quantity removed`() {
        // Given
        val cartId = CartId("cart-456")
        val productId = ProductId("PROD-B")
        val invalidQuantityRemoved = 0  // Must be positive
        val remainingQuantity = 1
        val version = EventVersion(3)

        // When & Then
        assertFailsWith<IllegalArgumentException> {
            ItemRemovedEvent(cartId, productId, invalidQuantityRemoved, remainingQuantity, version)
        }
    }

    @Test
    fun `should reject ItemRemovedEvent with negative remaining quantity`() {
        // Given
        val cartId = CartId("cart-789")
        val productId = ProductId("PROD-C")
        val quantityRemoved = 3
        val negativeRemainingQuantity = -1  // Cannot be negative
        val version = EventVersion(2)

        // When & Then
        assertFailsWith<IllegalArgumentException> {
            ItemRemovedEvent(cartId, productId, quantityRemoved, negativeRemainingQuantity, version)
        }
    }

    @Test
    fun `should allow ItemRemovedEvent with zero remaining quantity`() {
        // Given
        val cartId = CartId("cart-complete-removal")
        val productId = ProductId("PROD-D")
        val quantityRemoved = 5
        val zeroRemainingQuantity = 0  // Valid when item is completely removed
        val version = EventVersion(4)

        // When
        val event = ItemRemovedEvent(cartId, productId, quantityRemoved, zeroRemainingQuantity, version)

        // Then
        assertEquals(0, event.remainingQuantity)
        assertEquals(quantityRemoved, event.quantity)
    }

    @Test
    fun `should use companion object factory method`() {
        // Given
        val cartId = CartId("factory-test")
        val productId = ProductId("FACTORY-PROD")
        val removedQuantity = 3
        val quantityAfterRemoval = 2
        val newVersion = EventVersion(10)

        // When
        val event = ItemRemovedEvent.create(cartId, productId, removedQuantity, quantityAfterRemoval, newVersion)

        // Then
        assertEquals(cartId, event.cartId)
        assertEquals(productId, event.productId)
        assertEquals(removedQuantity, event.quantity)
        assertEquals(quantityAfterRemoval, event.remainingQuantity)
        assertEquals(newVersion, event.version)
    }
}
