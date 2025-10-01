# API Contracts: Remove Item from Cart

**Feature**: 002-removing-an-item  
**Date**: September 30, 2025

## Domain Service Contract

### CartService.removeItem()

**Purpose**: Core domain service for removing items from cart  
**Module**: domain

```kotlin
interface CartService {
    /**
     * Remove one unit of specified product from cart
     * @param command RemoveItemCommand with cartId, productId, expectedVersion
     * @return Updated cart state after removal
     * @throws ItemNotInCartException if product not in cart
     * @throws EmptyCartException if cart is empty  
     * @throws ConcurrencyException if version mismatch
     */
    suspend fun removeItem(command: RemoveItemCommand): Cart
}
```

**Pre-conditions**:
- Valid RemoveItemCommand with non-null parameters
- Cart exists with specified cartId
- Expected version matches current cart version

**Post-conditions**:
- Item quantity decremented by 1, OR item removed if quantity was 1
- ItemRemovedEvent appended to event store
- Cart version incremented
- Cart projection updated

**Error Conditions**:
- ItemNotInCartException: Product ID not found in cart
- EmptyCartException: No items in cart to remove
- ConcurrencyException: Version mismatch detected

## CLI Contract

### Remove Item Command

**Purpose**: CLI interface for removing items from cart  
**Module**: cli-app

```bash
# Command format
cart remove --cart-id <cart-id> --product-id <product-id> [--version <version>]

# Success output
Removed 1 unit of PROD-123 from cart CART-456
Remaining quantity: 2

# Item completely removed output  
Removed last unit of PROD-123 from cart CART-456
Item removed from cart

# Error outputs
Error: Item PROD-123 not found in cart CART-456
Error: Cannot remove items from empty cart CART-456
Error: Cart version mismatch - cart was modified by another operation
```

**Exit Codes**:
- 0: Success
- 1: Validation error (item not found, empty cart)
- 2: Concurrency error (version mismatch)  
- 3: System error

## HTTP API Contract

### POST /carts/{cartId}/remove-item

**Purpose**: HTTP API endpoint for removing items from cart  
**Module**: http-app

**Request**:
```json
POST /carts/{cartId}/remove-item
Content-Type: application/json

{
  "productId": "PROD-123",
  "expectedVersion": 5
}
```

**Success Response (200 OK)**:
```json
{
  "cartId": "CART-456", 
  "version": 6,
  "items": [
    {
      "productId": "PROD-123",
      "quantity": 2
    },
    {
      "productId": "PROD-789", 
      "quantity": 1
    }
  ],
  "removedItem": {
    "productId": "PROD-123",
    "quantityRemoved": 1,
    "remainingQuantity": 2
  }
}
```

**Item Completely Removed Response (200 OK)**:
```json
{
  "cartId": "CART-456",
  "version": 6, 
  "items": [
    {
      "productId": "PROD-789",
      "quantity": 1  
    }
  ],
  "removedItem": {
    "productId": "PROD-123",
    "quantityRemoved": 1,
    "remainingQuantity": 0
  }
}
```

**Error Responses**:

**400 Bad Request - Item Not Found**:
```json
{
  "error": "ITEM_NOT_IN_CART",
  "message": "Item PROD-123 not found in cart CART-456",
  "cartId": "CART-456",
  "productId": "PROD-123"
}
```

**400 Bad Request - Empty Cart**:
```json
{
  "error": "EMPTY_CART", 
  "message": "Cannot remove items from empty cart CART-456",
  "cartId": "CART-456"
}
```

**409 Conflict - Version Mismatch**:
```json
{
  "error": "VERSION_MISMATCH",
  "message": "Cart version mismatch - expected 5 but was 7", 
  "cartId": "CART-456",
  "expectedVersion": 5,
  "actualVersion": 7
}
```

**500 Internal Server Error**:
```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred while removing item"
}
```

## Event Store Contract

### ItemRemovedEvent Storage

**Purpose**: Event persistence contract for item removal events  
**Module**: infrastructure

**Event Schema**:
```json
{
  "eventType": "ItemRemovedEvent",
  "aggregateId": "CART-456",
  "version": 6,
  "timestamp": "2025-09-30T14:30:45.123Z",
  "payload": {
    "productId": "PROD-123", 
    "quantityRemoved": 1,
    "remainingQuantity": 2
  }
}
```

**Storage Requirements**:
- Events stored in chronological order by timestamp
- Version numbers must be sequential per aggregate
- Concurrent append detection via version checking
- Event payload immutable after storage

## Performance Contracts

### Response Time Requirements

**Domain Operations**: <1ms for 95% of operations  
**CLI Commands**: <50ms total execution time  
**HTTP API**: <100ms response time for 95% of requests  
**Event Storage**: <10ms for event append operations

### Throughput Requirements  

**Concurrent Operations**: Support 100 concurrent remove operations  
**Event Rate**: Handle 1000 events/second append rate  
**Memory Usage**: <100MB for 10,000 cart operations
