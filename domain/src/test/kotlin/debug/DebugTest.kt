package debug

import cart.CartId
import cart.ProductId
import commands.RemoveItemCommand
import events.EventVersion
import kotlin.test.Test
import kotlin.test.assertFailsWith

class DebugTest {
    @Test
    fun `debug test validation`() {
        val productId = ProductId("PROD-A")
        val version = EventVersion(1)
        
        // This should throw IllegalArgumentException when creating empty CartId
        assertFailsWith<IllegalArgumentException> {
            val emptyCartId = CartId("")
            RemoveItemCommand(emptyCartId, productId, version)
        }
    }
}
