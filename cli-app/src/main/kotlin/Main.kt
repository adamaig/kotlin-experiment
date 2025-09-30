import cart.ProductId
import commands.AddItemToCart
import eventstore.InMemoryEventStore
import services.CartServiceImpl

fun main() {
    println("🛒 Kotlin Cart Demo - Event Sourcing Implementation")
    println("=" .repeat(50))
    
    // Initialize the event store and service
    val eventStore = InMemoryEventStore()
    val cartService = CartServiceImpl(eventStore)
    
    try {
        println("📦 Creating new cart and adding first item...")
        
        // Add first item (creates new cart)
        val result1 = cartService.addItemToCart(
            AddItemToCart(
                cartId = null, // Create new cart
                productId = ProductId("laptop-123"),
                quantity = 1
            )
        )
        
        if (result1.isSuccess) {
            val addItemResult = result1.getOrThrow()
            println("✅ Cart created: ${addItemResult.cart.cartId.value}")
            println("   Total items: ${addItemResult.cart.totalQuantity}")
            println("   Events generated: ${addItemResult.events.size}")
            addItemResult.events.forEach { event ->
                println("   - ${event::class.simpleName}")
            }
            
            // Add second item to existing cart
            println("\n📦 Adding second item to existing cart...")
            val result2 = cartService.addItemToCart(
                AddItemToCart(
                    cartId = addItemResult.cart.cartId,
                    productId = ProductId("mouse-456"), 
                    quantity = 1
                )
            )
            
            if (result2.isSuccess) {
                val addItemResult2 = result2.getOrThrow()
                println("✅ Item added successfully!")
                println("   Total items: ${addItemResult2.cart.totalQuantity}")
                println("   Line items: ${addItemResult2.cart.lineItems.size}")
                addItemResult2.cart.lineItems.forEach { (productId, lineItem) ->
                    println("   - ${productId.value}: quantity ${lineItem.quantity}")
                }
                
                // Try to add item that would exceed capacity
                println("\n📦 Attempting to add items that would exceed capacity...")
                val result3 = cartService.addItemToCart(
                    AddItemToCart(
                        cartId = addItemResult2.cart.cartId,
                        productId = ProductId("keyboard-789"),
                        quantity = 2 // This would make total = 4, exceeding limit of 3
                    )
                )
                
                if (result3.isFailure) {
                    println("❌ Expected failure: ${result3.exceptionOrNull()?.message}")
                } else {
                    println("❌ Unexpected success - capacity constraint failed!")
                }
                
                // Add one more item to reach exactly capacity
                println("\n📦 Adding one more item to reach capacity...")
                val result4 = cartService.addItemToCart(
                    AddItemToCart(
                        cartId = addItemResult2.cart.cartId,
                        productId = ProductId("keyboard-789"),
                        quantity = 1 // This should work (total = 3)
                    )
                )
                
                if (result4.isSuccess) {
                    val finalResult = result4.getOrThrow()
                    println("✅ Cart at capacity!")
                    println("   Total items: ${finalResult.cart.totalQuantity}")
                    println("   At capacity: ${finalResult.cart.isAtCapacity}")
                    finalResult.cart.lineItems.forEach { (productId, lineItem) ->
                        println("   - ${productId.value}: quantity ${lineItem.quantity}")
                    }
                }
                
            } else {
                println("❌ Failed to add second item: ${result2.exceptionOrNull()?.message}")
            }
            
        } else {
            println("❌ Failed to create cart: ${result1.exceptionOrNull()?.message}")
        }
        
    } catch (e: Exception) {
        println("❌ Error: ${e.message}")
        e.printStackTrace()
    }
    
    println("\n🎉 Demo completed!")
}
