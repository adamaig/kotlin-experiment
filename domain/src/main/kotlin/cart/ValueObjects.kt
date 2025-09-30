package cart

import java.util.*

@JvmInline
value class ProductId(val value: String) {
    init {
        require(value.isNotBlank()) { "Product ID cannot be blank" }
    }
}

@JvmInline  
value class CartId(val value: String) {
    init {
        require(value.isNotBlank()) { "Cart ID cannot be blank" }
    }
    
    companion object {
        fun generate(): CartId = CartId(UUID.randomUUID().toString())
    }
}
