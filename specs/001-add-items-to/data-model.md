# Data Model: Add Items to Cart

## Domain Entities

### Cart (Aggregate Root)
**Purpose**: Manages collection of items with capacity constraints
**Identity**: CartId (UUID)
**Invariants**: 
- Total quantity across all line items ≤ 3
- Cannot exceed capacity when adding items
- Line items must have positive quantities

**Properties**:
```kotlin
data class Cart(
    val cartId: CartId,
    val lineItems: Map<ProductId, LineItem>,
    val version: EventVersion
) {
    val totalQuantity: Int get() = lineItems.values.sumOf { it.quantity }
    val isAtCapacity: Boolean get() = totalQuantity >= 3
    
    fun canAddItem(quantity: Int): Boolean = 
        totalQuantity + quantity <= 3
}
```

**State Transitions**:
- Empty → Contains Items (via CartCreated + ItemAddedToCart)
- Contains Items → Contains Items (via ItemAddedToCart with quantity increment)
- Contains Items → Error State (via AddItemToCart command when capacity exceeded)

### LineItem (Entity within Cart)
**Purpose**: Represents specific product with quantity in cart
**Identity**: ProductId (within cart context)

**Properties**:
```kotlin
data class LineItem(
    val productId: ProductId,
    val quantity: Int
) {
    init {
        require(quantity > 0) { "Quantity must be positive" }
    }
    
    fun addQuantity(additionalQuantity: Int): LineItem =
        copy(quantity = quantity + additionalQuantity)
}
```

### Item (Value Object)
**Purpose**: Reference to a product that can be added to cart
**Identity**: ProductId

**Properties**:
```kotlin
data class Item(
    val productId: ProductId
) {
    // Additional properties (name, price, etc.) not required for cart functionality
    // Can be extended in future without breaking cart domain
}
```

## Value Objects

### ProductId
```kotlin
@JvmInline
value class ProductId(val value: String) {
    init {
        require(value.isNotBlank()) { "Product ID cannot be blank" }
    }
}
```

### CartId  
```kotlin
@JvmInline
value class CartId(val value: String) {
    init {
        require(value.isNotBlank()) { "Cart ID cannot be blank" }
    }
    
    companion object {
        fun generate(): CartId = CartId(UUID.randomUUID().toString())
    }
}
```

### EventVersion
```kotlin
@JvmInline
value class EventVersion(val value: Long) {
    init {
        require(value >= 0) { "Version cannot be negative" }
    }
    
    fun increment(): EventVersion = EventVersion(value + 1)
    
    companion object {
        val initial = EventVersion(0)
    }
}
```

## Domain Events

### CartCreated
```kotlin
data class CartCreated(
    val cartId: CartId,
    val timestamp: Instant = Instant.now()
) : DomainEvent
```

### ItemAddedToCart
```kotlin
data class ItemAddedToCart(
    val cartId: CartId,
    val productId: ProductId,
    val quantity: Int,
    val newTotalQuantity: Int,
    val timestamp: Instant = Instant.now()
) : DomainEvent {
    init {
        require(quantity > 0) { "Added quantity must be positive" }
        require(newTotalQuantity <= 3) { "Total quantity cannot exceed capacity" }
    }
}
```

## Commands

### AddItemToCart
```kotlin
data class AddItemToCart(
    val cartId: CartId?,  // null when creating new cart
    val productId: ProductId,
    val quantity: Int = 1
) {
    init {
        require(quantity > 0) { "Quantity must be positive" }
    }
}
```

## Domain Services

### CartService
**Purpose**: Orchestrates cart operations and enforces business rules

```kotlin
class CartService(
    private val eventStore: EventStore,
    private val cartRepository: CartRepository
) {
    fun addItemToCart(command: AddItemToCart): Result<List<DomainEvent>> {
        // Implementation will:
        // 1. Load or create cart
        // 2. Validate capacity constraints
        // 3. Apply business rules
        // 4. Generate appropriate events
        // 5. Handle optimistic locking
    }
}
```

## Repositories

### CartRepository (Interface)
```kotlin
interface CartRepository {
    fun findById(cartId: CartId): Cart?
    fun save(cart: Cart, expectedVersion: EventVersion): Result<Unit>
}
```

## Business Rules

1. **Cart Creation**: New cart created automatically when first item added (cartId is null)
2. **Duplicate Handling**: Same ProductId increments quantity of existing LineItem
3. **Capacity Validation**: Total quantity across all LineItems cannot exceed 3
4. **Capacity Overflow**: Complete rejection when adding would exceed capacity
5. **Concurrency Control**: Optimistic locking using EventVersion for conflict detection
6. **Quantity Constraints**: All quantities must be positive integers

## Event Store Schema

### Event Envelope
```kotlin
data class EventEnvelope(
    val aggregateId: String,      // CartId as string
    val aggregateType: String,    // "Cart"
    val eventType: String,        // Event class name
    val eventData: String,        // JSON serialized event
    val version: EventVersion,    // Aggregate version after this event
    val timestamp: Instant
)
```

### Event Stream Structure
- Stream per Cart aggregate: `cart-{cartId}`
- Events ordered by version within stream
- Global ordering maintained across all streams
- Optimistic concurrency through expected version validation
