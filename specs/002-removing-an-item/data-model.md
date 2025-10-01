# Data Model: Remove Item from Cart

**Feature**: 002-removing-an-item  
**Date**: September 30, 2025

## Domain Entities

### Cart (Existing - Extensions Required)

**Purpose**: Aggregate root managing shopping cart state and operations  
**Location**: `domain/src/main/kotlin/cart/Cart.kt`

**New Method Required**:
```kotlin
fun removeItem(productId: ProductId, expectedVersion: EventVersion): List<DomainEvent>
```

**Business Rules**:
- Must validate productId exists in cart before removal
- Must decrement quantity by exactly 1
- Must remove item entirely when quantity reaches 0
- Must check expectedVersion for concurrency control
- Must generate ItemRemovedEvent for successful operations

### ProductId (Existing)

**Purpose**: Value object identifying products in cart operations  
**Location**: `domain/src/main/kotlin/cart/ProductId.kt`  
**Usage**: Parameter for removeItem command, event payload

### LineItem (Existing - Behavior Extensions)

**Purpose**: Represents product and quantity pair within cart  
**Location**: `domain/src/main/kotlin/cart/LineItem.kt`

**Required Extensions**:
- Quantity decrement validation
- Zero quantity detection for removal

## Command Objects

### RemoveItemCommand (New)

**Purpose**: Encapsulates remove item request with validation  
**Location**: `domain/src/main/kotlin/commands/RemoveItemCommand.kt`

**Structure**:
```kotlin
data class RemoveItemCommand(
    val cartId: CartId,
    val productId: ProductId,
    val expectedVersion: EventVersion
) : Command
```

**Validation Rules**:
- cartId must be valid (non-empty)
- productId must be valid (non-empty) 
- expectedVersion must be valid (non-negative)

## Domain Events

### ItemRemovedEvent (New)

**Purpose**: Records successful item removal from cart  
**Location**: `domain/src/main/kotlin/events/ItemRemovedEvent.kt`

**Structure**:
```kotlin
data class ItemRemovedEvent(
    override val aggregateId: CartId,
    override val version: EventVersion,
    val productId: ProductId,
    val quantityRemoved: Int,
    val remainingQuantity: Int,
    val timestamp: Instant = Clock.System.now()
) : DomainEvent
```

**Event Payload**:
- aggregateId: Identifies which cart was modified
- version: Event version for ordering and concurrency
- productId: Which product was removed  
- quantityRemoved: Always 1 per business rules
- remainingQuantity: Final quantity after removal (0 means item removed entirely)
- timestamp: When the removal occurred

## Exception Types

### ItemNotInCartException (New)

**Purpose**: Validation error when attempting to remove non-existent item  
**Location**: `domain/src/main/kotlin/cart/ItemNotInCartException.kt`

**Structure**:
```kotlin
class ItemNotInCartException(
    val cartId: CartId,
    val productId: ProductId
) : DomainException("Item ${productId.value} not found in cart ${cartId.value}")
```

### EmptyCartException (New)

**Purpose**: Validation error when attempting to remove from empty cart  
**Location**: `domain/src/main/kotlin/cart/EmptyCartException.kt`

**Structure**:
```kotlin  
class EmptyCartException(
    val cartId: CartId
) : DomainException("Cannot remove items from empty cart ${cartId.value}")
```

## State Transitions

### Cart State Changes

**Before Removal**:
```
Cart(id=cart-123, items=[
    LineItem(productId=PROD-A, quantity=3),
    LineItem(productId=PROD-B, quantity=1)
], version=5)
```

**After Removing PROD-A**:
```
Cart(id=cart-123, items=[
    LineItem(productId=PROD-A, quantity=2),  // Decremented
    LineItem(productId=PROD-B, quantity=1)   // Unchanged
], version=6)  // Version incremented
```

**After Removing Last PROD-B**:
```
Cart(id=cart-123, items=[
    LineItem(productId=PROD-A, quantity=2)   // PROD-B removed entirely
], version=7)  // Version incremented
```

## Relationships

### Aggregate Boundaries
- Cart is the aggregate root
- LineItems are entities within Cart aggregate
- ProductId is a value object shared across contexts
- Commands operate on Cart aggregate
- Events are published by Cart aggregate

### Command → Event Flow
1. RemoveItemCommand validates input parameters
2. Cart.removeItem() executes business logic
3. ItemRemovedEvent generated on success
4. Exception thrown on validation failure
5. EventStore appends event with version check
6. Cart projection updated with new state

## Validation Matrix

| Input Condition | Cart State | Expected Outcome |
|----------------|------------|------------------|
| Valid productId, qty > 1 | Item exists with quantity > 1 | Quantity decremented, ItemRemovedEvent |
| Valid productId, qty = 1 | Item exists with quantity = 1 | Item removed entirely, ItemRemovedEvent |
| Invalid productId | Item does not exist | ItemNotInCartException |
| Any productId | Empty cart | EmptyCartException |
| Version mismatch | Any state | ConcurrencyException (existing) |
