# Cart API Contracts

## HTTP API Specification

### POST /carts/{cartId}/items
**Purpose**: Add item to existing cart
**Method**: POST
**Path**: `/carts/{cartId}/items`

**Request**:
```json
{
  "productId": "string",
  "quantity": 1
}
```

**Responses**:
- **200 OK**: Item successfully added
```json
{
  "cartId": "string",
  "totalQuantity": 2,
  "lineItems": [
    {
      "productId": "string", 
      "quantity": 2
    }
  ]
}
```

- **400 Bad Request**: Capacity exceeded
```json
{
  "error": "A cart cannot hold more than three (3) items.",
  "currentQuantity": 3,
  "attemptedQuantity": 1
}
```

- **404 Not Found**: Cart not found
```json
{
  "error": "Cart not found",
  "cartId": "string"
}
```

- **409 Conflict**: Concurrent modification
```json
{
  "error": "Cart was modified by another operation. Please retry.",
  "currentVersion": 5
}
```

### POST /carts/items
**Purpose**: Add item to cart (creates cart if needed)
**Method**: POST  
**Path**: `/carts/items`

**Request**:
```json
{
  "productId": "string",
  "quantity": 1
}
```

**Responses**:
- **201 Created**: Cart created and item added
```json
{
  "cartId": "string",
  "totalQuantity": 1,
  "lineItems": [
    {
      "productId": "string",
      "quantity": 1  
    }
  ]
}
```

- **400 Bad Request**: Invalid request
```json
{
  "error": "Product ID cannot be blank"
}
```

### GET /carts/{cartId}
**Purpose**: Retrieve cart contents
**Method**: GET
**Path**: `/carts/{cartId}`

**Responses**:
- **200 OK**: Cart found
```json
{
  "cartId": "string",
  "totalQuantity": 2,
  "lineItems": [
    {
      "productId": "prod-1",
      "quantity": 1
    },
    {
      "productId": "prod-2", 
      "quantity": 1
    }
  ],
  "version": 3
}
```

- **404 Not Found**: Cart not found
```json
{
  "error": "Cart not found",
  "cartId": "string"
}
```

## CLI Interface Specification

### add-item command
**Purpose**: Add item to cart via CLI
**Usage**: `cart add-item [--cart-id <id>] --product-id <id> [--quantity <n>]`

**Parameters**:
- `--cart-id`: Optional. Cart ID. If omitted, creates new cart
- `--product-id`: Required. Product identifier  
- `--quantity`: Optional. Quantity to add (default: 1)

**Examples**:
```bash
# Create new cart and add item
cart add-item --product-id "prod-123"

# Add item to existing cart
cart add-item --cart-id "cart-456" --product-id "prod-789" --quantity 2

# Add multiple quantity
cart add-item --product-id "prod-abc" --quantity 3
```

**Output - Success**:
```
Cart: cart-123
Total Quantity: 2
Items:
- Product ID: prod-123, Quantity: 2
```

**Output - Error (Capacity)**:
```
Error: A cart cannot hold more than three (3) items.
Current quantity: 3, Attempted to add: 1
```

**Output - Error (Not Found)**:
```
Error: Cart 'cart-999' not found
```

### show-cart command  
**Purpose**: Display cart contents
**Usage**: `cart show-cart --cart-id <id>`

**Parameters**:
- `--cart-id`: Required. Cart ID to display

**Output - Success**:
```
Cart: cart-123
Total Quantity: 2
Items:
- Product ID: prod-abc, Quantity: 1
- Product ID: prod-def, Quantity: 1
Version: 3
```

**Output - Error**:
```
Error: Cart 'cart-999' not found
```

## Domain Service Contracts

### CartService Interface
```kotlin
interface CartService {
    /**
     * Add item to cart, creating cart if needed
     * @param command AddItemToCart command
     * @return Result containing success events or failure reason
     */
    suspend fun addItemToCart(command: AddItemToCart): Result<AddItemResult>
    
    /**
     * Retrieve cart by ID
     * @param cartId Cart identifier
     * @return Cart if found, null otherwise
     */
    suspend fun getCart(cartId: CartId): Cart?
}

data class AddItemResult(
    val events: List<DomainEvent>,
    val updatedCart: Cart
)
```

### EventStore Interface  
```kotlin
interface EventStore {
    /**
     * Append events to aggregate stream
     * @param aggregateId Aggregate identifier
     * @param events Events to append
     * @param expectedVersion Expected current version for optimistic locking
     * @return Result with new version or conflict error
     */
    suspend fun appendEvents(
        aggregateId: String,
        events: List<DomainEvent>, 
        expectedVersion: EventVersion
    ): Result<EventVersion>
    
    /**
     * Read events from aggregate stream
     * @param aggregateId Aggregate identifier  
     * @param fromVersion Start reading from this version (inclusive)
     * @return List of events in order
     */
    suspend fun readEvents(
        aggregateId: String,
        fromVersion: EventVersion = EventVersion.initial
    ): List<EventEnvelope>
    
    /**
     * Subscribe to new events
     * @param handler Event handler function
     */
    fun subscribe(handler: (EventEnvelope) -> Unit)
}
```

## Error Handling Contracts

### Domain Exceptions
```kotlin
sealed class CartDomainException(message: String) : Exception(message)

class CartCapacityExceededException(
    val currentQuantity: Int,
    val attemptedQuantity: Int
) : CartDomainException("A cart cannot hold more than three (3) items.")

class CartNotFoundException(
    val cartId: CartId
) : CartDomainException("Cart '${cartId.value}' not found")

class OptimisticLockException(
    val expectedVersion: EventVersion,
    val actualVersion: EventVersion
) : CartDomainException("Cart was modified by another operation. Please retry.")

class InvalidQuantityException(
    val quantity: Int
) : CartDomainException("Quantity must be positive, got: $quantity")
```

### Result Types
```kotlin
sealed class Result<out T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure(val exception: Exception) : Result<Nothing>
    
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }
    
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(value)
        return this
    }
    
    inline fun onFailure(action: (Exception) -> Unit): Result<T> {
        if (this is Failure) action(exception)
        return this
    }
}
```
