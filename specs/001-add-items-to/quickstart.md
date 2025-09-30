# Quickstart: Add Items to Cart

## Overview
This quickstart demonstrates the cart functionality through failing tests that validate the acceptance scenarios. Follow the TDD approach: write failing tests, implement minimal code to pass, then refactor.

## Prerequisites
- Kotlin 1.9+ with JVM target
- Gradle 8.0+
- IDE with Kotlin support

## Project Setup

### 1. Initialize Gradle Multi-Module Project
```bash
# Repository should already exist, verify structure:
ls -la
# Should show: build.gradle.kts, settings.gradle.kts, domain/, cli-app/, etc.
```

### 2. Build and Verify
```bash
./gradlew clean build
# Should compile successfully (may have failing tests initially)
```

## TDD Acceptance Test Scenarios

### Scenario 1: Create Cart and Add First Item
**Story**: Given no existing cart, when I add an item, then a new cart is created and the item is added to it

```kotlin
// File: domain/src/test/kotlin/cart/CartAcceptanceTest.kt
@Test
fun `should create cart and add first item`() {
    // Arrange
    val cartService = CartService(inMemoryEventStore, cartRepository)
    val command = AddItemToCart(
        cartId = null,  // No existing cart
        productId = ProductId("prod-123"),
        quantity = 1
    )
    
    // Act
    val result = cartService.addItemToCart(command)
    
    // Assert
    assertThat(result).isInstanceOf<Result.Success>()
    val addItemResult = result.getOrNull()!!
    
    // Verify events generated
    assertThat(addItemResult.events).hasSize(2)
    assertThat(addItemResult.events[0]).isInstanceOf<CartCreated>()
    assertThat(addItemResult.events[1]).isInstanceOf<ItemAddedToCart>()
    
    // Verify cart state
    val cart = addItemResult.updatedCart
    assertThat(cart.totalQuantity).isEqualTo(1)
    assertThat(cart.lineItems).hasSize(1)
    assertThat(cart.lineItems[ProductId("prod-123")]?.quantity).isEqualTo(1)
}
```

### Scenario 2: Add Item to Existing Cart
**Story**: Given a cart with 1 item, when I add another item, then the item is successfully added

```kotlin
@Test  
fun `should add item to existing cart`() {
    // Arrange - Create cart with existing item
    val existingCartId = CartId.generate()
    val existingCart = createCartWithItem(existingCartId, ProductId("prod-1"), 1)
    
    val command = AddItemToCart(
        cartId = existingCartId,
        productId = ProductId("prod-2"), 
        quantity = 1
    )
    
    // Act
    val result = cartService.addItemToCart(command)
    
    // Assert
    assertThat(result).isInstanceOf<Result.Success>()
    val updatedCart = result.getOrNull()!!.updatedCart
    
    assertThat(updatedCart.totalQuantity).isEqualTo(2)
    assertThat(updatedCart.lineItems).hasSize(2)
    assertThat(updatedCart.lineItems[ProductId("prod-1")]?.quantity).isEqualTo(1)
    assertThat(updatedCart.lineItems[ProductId("prod-2")]?.quantity).isEqualTo(1)
}
```

### Scenario 3: Increment Quantity for Same Item
**Story**: Given a cart with 1 item (quantity 2), when I add the same item again, then the quantity is incremented

```kotlin
@Test
fun `should increment quantity when adding same item`() {
    // Arrange
    val cartId = CartId.generate()
    val existingCart = createCartWithItem(cartId, ProductId("prod-1"), 2)
    
    val command = AddItemToCart(
        cartId = cartId,
        productId = ProductId("prod-1"), // Same product
        quantity = 1
    )
    
    // Act  
    val result = cartService.addItemToCart(command)
    
    // Assert
    assertThat(result).isInstanceOf<Result.Success>()
    val updatedCart = result.getOrNull()!!.updatedCart
    
    assertThat(updatedCart.totalQuantity).isEqualTo(3)
    assertThat(updatedCart.lineItems).hasSize(1) // Still one line item
    assertThat(updatedCart.lineItems[ProductId("prod-1")]?.quantity).isEqualTo(3)
}
```

### Scenario 4: Reject When Capacity Exceeded
**Story**: Given a cart with 3 items (at capacity), when I attempt to add another item, then the system rejects the request

```kotlin
@Test
fun `should reject when capacity exceeded`() {
    // Arrange - Cart at capacity
    val cartId = CartId.generate()
    val fullCart = createCartWithItems(cartId, listOf(
        ProductId("prod-1") to 1,
        ProductId("prod-2") to 1, 
        ProductId("prod-3") to 1
    )) // Total quantity = 3
    
    val command = AddItemToCart(
        cartId = cartId,
        productId = ProductId("prod-4"),
        quantity = 1
    )
    
    // Act
    val result = cartService.addItemToCart(command)
    
    // Assert
    assertThat(result).isInstanceOf<Result.Failure>()
    val exception = result.exceptionOrNull()
    assertThat(exception).isInstanceOf<CartCapacityExceededException>()
    assertThat(exception.message).isEqualTo("A cart cannot hold more than three (3) items.")
}
```

### Scenario 5: Handle Concurrent Operations
**Story**: Given two concurrent operations on a cart, when both attempt to add items, then one succeeds and the other detects conflict

```kotlin
@Test
fun `should handle concurrent operations with optimistic locking`() {
    // Arrange
    val cartId = CartId.generate() 
    val initialCart = createCartWithItem(cartId, ProductId("prod-1"), 1)
    
    // Simulate concurrent commands
    val command1 = AddItemToCart(cartId, ProductId("prod-2"), 1)
    val command2 = AddItemToCart(cartId, ProductId("prod-3"), 1)
    
    // Act - Execute first command
    val result1 = cartService.addItemToCart(command1)
    
    // Act - Execute second command (should detect conflict)
    val result2 = cartService.addItemToCart(command2)
    
    // Assert
    assertThat(result1).isInstanceOf<Result.Success>()
    assertThat(result2).isInstanceOf<Result.Failure>()
    
    val conflictException = result2.exceptionOrNull()
    assertThat(conflictException).isInstanceOf<OptimisticLockException>()
}
```

## Running the Tests

### 1. Run Domain Tests
```bash
./gradlew :domain:test
# Expected: All tests should FAIL initially (red phase)
```

### 2. Implement Domain Model
Follow the TDD cycle:
1. **Red**: Write failing test
2. **Green**: Implement minimal code to pass
3. **Refactor**: Improve design while keeping tests green

Start with basic data classes:
```kotlin
// File: domain/src/main/kotlin/cart/Cart.kt
data class Cart(
    val cartId: CartId,
    val lineItems: Map<ProductId, LineItem> = emptyMap(),
    val version: EventVersion = EventVersion.initial
) {
    // Implement methods to make tests pass
}
```

### 3. Verify Green Phase
```bash
./gradlew :domain:test
# Expected: All tests should PASS after implementation
```

### 4. Run CLI Integration Tests
```bash 
./gradlew :cli-app:test
# Test CLI commands against domain implementation
```

### 5. Run HTTP API Tests  
```bash
./gradlew :http-app:test  
# Test HTTP endpoints against domain implementation
```

## Validation Checklist

- [ ] All domain unit tests pass
- [ ] Cart creation works correctly
- [ ] Item addition respects quantity limits
- [ ] Duplicate items increment quantity  
- [ ] Capacity overflow is rejected with correct error message
- [ ] Optimistic locking prevents concurrent modifications
- [ ] Event store preserves complete audit trail
- [ ] CLI interface works end-to-end
- [ ] HTTP API responds correctly
- [ ] Code coverage > 90% for domain logic

## Next Steps

1. **Extend Domain Model**: Add more cart operations (remove item, clear cart)
2. **Add Persistence**: Implement file-based or database event store
3. **Add Projections**: Create read models for cart queries
4. **Performance Testing**: Validate in-memory event store performance
5. **API Documentation**: Generate OpenAPI specs from contracts

## Troubleshooting

### Tests Fail with Missing Dependencies
```bash
# Verify Gradle configuration
./gradlew dependencies
# Check module dependencies in build.gradle.kts files
```

### OptimisticLockException Not Working
```bash
# Verify EventStore implementation uses version comparison
# Check that CartRepository validates expectedVersion
```

### Performance Issues
```bash
# Profile event store operations
# Consider indexing strategies for large event streams
```

This quickstart provides a foundation for TDD development following the constitutional requirements of domain purity, event sourcing, and architectural separation.
