package cart

import events.EventVersion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProductIdTest {

    @Test
    fun `should create ProductId with valid value`() {
        val productId = ProductId("prod-123")
        assertEquals("prod-123", productId.value)
    }

    @Test
    fun `should reject blank ProductId`() {
        assertFailsWith<IllegalArgumentException> {
            ProductId("")
        }
    }

    @Test
    fun `should reject whitespace-only ProductId`() {
        assertFailsWith<IllegalArgumentException> {
            ProductId("   ")
        }
    }

    @Test
    fun `should be equal when values are equal`() {
        val productId1 = ProductId("prod-123")
        val productId2 = ProductId("prod-123")
        assertEquals(productId1, productId2)
    }

    @Test
    fun `should have consistent hashCode for equal values`() {
        val productId1 = ProductId("prod-123")
        val productId2 = ProductId("prod-123")
        assertEquals(productId1.hashCode(), productId2.hashCode())
    }
}

class CartIdTest {

    @Test
    fun `should create CartId with valid value`() {
        val cartId = CartId("cart-456")
        assertEquals("cart-456", cartId.value)
    }

    @Test
    fun `should reject blank CartId`() {
        assertFailsWith<IllegalArgumentException> {
            CartId("")
        }
    }

    @Test
    fun `should reject whitespace-only CartId`() {
        assertFailsWith<IllegalArgumentException> {
            CartId("   ")
        }
    }

    @Test
    fun `should generate unique CartId`() {
        val cartId1 = CartId.generate()
        val cartId2 = CartId.generate()
        assertFalse(cartId1.value == cartId2.value)
    }

    @Test
    fun `should be equal when values are equal`() {
        val cartId1 = CartId("cart-123")
        val cartId2 = CartId("cart-123")
        assertEquals(cartId1, cartId2)
    }
}

class EventVersionTest {

    @Test
    fun `should create EventVersion with valid value`() {
        val version = EventVersion(5)
        assertEquals(5, version.value)
    }

    @Test
    fun `should create initial version as zero`() {
        val initial = EventVersion.initial
        assertEquals(0, initial.value)
    }

    @Test
    fun `should increment version correctly`() {
        val version = EventVersion(3)
        val incremented = version.increment()
        assertEquals(4, incremented.value)
    }

    @Test
    fun `should reject negative version`() {
        assertFailsWith<IllegalArgumentException> {
            EventVersion(-1)
        }
    }

    @Test
    fun `should allow zero version`() {
        val version = EventVersion(0)
        assertEquals(0, version.value)
    }

    @Test
    fun `should be equal when values are equal`() {
        val version1 = EventVersion(3)
        val version2 = EventVersion(3)
        assertEquals(version1, version2)
    }
}
