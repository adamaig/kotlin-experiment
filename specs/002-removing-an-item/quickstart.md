# Quick Start: Remove Item from Cart

**Feature**: 002-removing-an-item  
**Date**: September 30, 2025

## Overview
This guide demonstrates how to test the remove item functionality across all application layers. Follow these steps to verify the implementation works correctly.

## Prerequisites

1. **Build the project**:
   ```bash
   cd /workspaces/kotlin-experiment
   ./gradlew build
   ```

2. **Verify existing cart functionality**:
   ```bash
   ./gradlew :cli-app:test
   ```

## Test Scenarios

### Scenario 1: Remove Item with Remaining Quantity

**Goal**: Remove one unit of an item that has multiple units in cart

**Setup**:
```bash
# Start with a cart containing multiple items
./gradlew :cli-app:run --args="cart create --cart-id test-cart-1"
./gradlew :cli-app:run --args="cart add --cart-id test-cart-1 --product-id PROD-A --quantity 3"
./gradlew :cli-app:run --args="cart add --cart-id test-cart-1 --product-id PROD-B --quantity 2"
```

**Execute**:
```bash  
./gradlew :cli-app:run --args="cart remove --cart-id test-cart-1 --product-id PROD-A"
```

**Expected Output**:
```
Removed 1 unit of PROD-A from cart test-cart-1
Remaining quantity: 2
```

**Verification**:
```bash
./gradlew :cli-app:run --args="cart show --cart-id test-cart-1"
# Should show: PROD-A quantity=2, PROD-B quantity=2
```

### Scenario 2: Remove Last Unit of Item

**Goal**: Remove the final unit of an item, causing complete removal

**Execute**:
```bash
./gradlew :cli-app:run --args="cart remove --cart-id test-cart-1 --product-id PROD-A"
./gradlew :cli-app:run --args="cart remove --cart-id test-cart-1 --product-id PROD-A"
```

**Expected Output**:
```
Removed last unit of PROD-A from cart test-cart-1
Item removed from cart
```

**Verification**:
```bash
./gradlew :cli-app:run --args="cart show --cart-id test-cart-1"  
# Should show: Only PROD-B quantity=2 (PROD-A completely gone)
```

### Scenario 3: Remove Non-Existent Item

**Goal**: Verify error handling for items not in cart

**Execute**:
```bash
./gradlew :cli-app:run --args="cart remove --cart-id test-cart-1 --product-id PROD-MISSING"
```

**Expected Output**:
```
Error: Item PROD-MISSING not found in cart test-cart-1
```

**Expected Exit Code**: 1

### Scenario 4: Remove from Empty Cart

**Goal**: Verify error handling for empty cart operations

**Setup**:
```bash
./gradlew :cli-app:run --args="cart create --cart-id empty-cart"
```

**Execute**:
```bash
./gradlew :cli-app:run --args="cart remove --cart-id empty-cart --product-id PROD-ANY"
```

**Expected Output**:
```
Error: Cannot remove items from empty cart empty-cart
```

**Expected Exit Code**: 1

### Scenario 5: Concurrent Remove Operations

**Goal**: Test version-based concurrency control

**Setup**:
```bash
./gradlew :cli-app:run --args="cart create --cart-id concurrent-cart"
./gradlew :cli-app:run --args="cart add --cart-id concurrent-cart --product-id PROD-X --quantity 5"
```

**Execute** (simulate concurrent operations):
```bash
# Terminal 1
./gradlew :cli-app:run --args="cart remove --cart-id concurrent-cart --product-id PROD-X --version 1" &

# Terminal 2 (quickly)  
./gradlew :cli-app:run --args="cart remove --cart-id concurrent-cart --product-id PROD-X --version 1" &
```

**Expected Behavior**:
- One operation succeeds: "Removed 1 unit of PROD-X"
- One operation fails: "Error: Cart version mismatch"
- Final state: PROD-X quantity=4, version=2

## HTTP API Testing

### Setup HTTP Server
```bash
./gradlew :http-app:run &
# Server starts on http://localhost:8080
```

### Test Remove Item API
```bash
# Create cart and add items
curl -X POST http://localhost:8080/carts/api-test-cart/create
curl -X POST http://localhost:8080/carts/api-test-cart/add-item \
  -H "Content-Type: application/json" \
  -d '{"productId": "PROD-API", "quantity": 3}'

# Remove item
curl -X POST http://localhost:8080/carts/api-test-cart/remove-item \
  -H "Content-Type: application/json" \
  -d '{"productId": "PROD-API", "expectedVersion": 2}'
```

**Expected Response**:
```json
{
  "cartId": "api-test-cart",
  "version": 3,
  "items": [
    {
      "productId": "PROD-API", 
      "quantity": 2
    }
  ],
  "removedItem": {
    "productId": "PROD-API",
    "quantityRemoved": 1, 
    "remainingQuantity": 2
  }
}
```

### Test Error Scenarios
```bash
# Test item not found
curl -X POST http://localhost:8080/carts/api-test-cart/remove-item \
  -H "Content-Type: application/json" \
  -d '{"productId": "MISSING-ITEM", "expectedVersion": 3}'

# Expected: 400 Bad Request with ITEM_NOT_IN_CART error
```

## Performance Validation

### Response Time Test
```bash
# Measure CLI response time  
time ./gradlew :cli-app:run --args="cart remove --cart-id perf-cart --product-id PROD-PERF"
# Should complete in <100ms

# Measure HTTP API response time
time curl -X POST http://localhost:8080/carts/perf-cart/remove-item \
  -H "Content-Type: application/json" \
  -d '{"productId": "PROD-PERF", "expectedVersion": 1}'
# Should respond in <100ms
```

### Load Test (Basic)
```bash  
# Run 50 concurrent remove operations
for i in {1..50}; do
  ./gradlew :cli-app:run --args="cart remove --cart-id load-cart-$i --product-id PROD-LOAD" &
done
wait

# All operations should complete within performance targets
```

## Unit Test Execution

### Run Domain Tests
```bash
./gradlew :domain:test --tests "*RemoveItem*"
```

**Expected**: All tests pass, >90% code coverage

### Run Integration Tests  
```bash
./gradlew :cli-app:test --tests "*RemoveItem*"
./gradlew :http-app:test --tests "*RemoveItem*"  
```

**Expected**: All integration scenarios pass

## Event Store Validation

### Verify Event Persistence
```bash
# Add and remove items to generate events
./gradlew :cli-app:run --args="cart add --cart-id event-cart --product-id EVENT-PROD --quantity 2"
./gradlew :cli-app:run --args="cart remove --cart-id event-cart --product-id EVENT-PROD"

# Check event store contents (implementation-specific command)
./gradlew :cli-app:run --args="events list --aggregate-id event-cart"
```

**Expected Events**:
1. `CartCreatedEvent` (version 0)
2. `ItemAddedEvent` (version 1) 
3. `ItemRemovedEvent` (version 2)

### Verify Event Ordering
```bash
# Perform sequence of operations
./gradlew :cli-app:run --args="cart add --cart-id order-cart --product-id A --quantity 1"
./gradlew :cli-app:run --args="cart add --cart-id order-cart --product-id B --quantity 1"  
./gradlew :cli-app:run --args="cart remove --cart-id order-cart --product-id A"
./gradlew :cli-app:run --args="cart remove --cart-id order-cart --product-id B"

# Events should be in chronological order with sequential version numbers
```

## Cleanup

```bash  
# Stop HTTP server if running
pkill -f "http-app"

# Clean test artifacts
./gradlew clean
```

## Success Criteria

✅ **All CLI scenarios execute without errors**  
✅ **HTTP API returns correct response codes and JSON**  
✅ **Error messages are clear and actionable**  
✅ **Performance targets met (<100ms response time)**  
✅ **Concurrent operations handled correctly**  
✅ **Events persisted in correct order**  
✅ **Unit and integration tests pass**  
✅ **No memory leaks or resource issues**

## Troubleshooting

**Issue**: "Command not found" errors  
**Solution**: Ensure Gradle daemon is running: `./gradlew --daemon`

**Issue**: Port 8080 already in use  
**Solution**: Kill existing processes: `pkill -f "8080"` or use different port

**Issue**: Version mismatch errors in testing  
**Solution**: Use `--version latest` flag or check current cart version first

**Issue**: Tests failing with compilation errors  
**Solution**: Rebuild project: `./gradlew clean build`
