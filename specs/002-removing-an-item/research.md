# Research: Remove Item from Cart

**Feature**: 002-removing-an-item  
**Date**: September 30, 2025

## Research Areas

### 1. Event Sourcing Patterns for Item Removal

**Decision**: Use ItemRemovedEvent with quantity tracking  
**Rationale**: Aligns with existing event modeling architecture. Allows precise audit trail of all quantity changes. Supports replay and temporal queries.  
**Alternatives considered**: 
- Direct state mutation: Rejected due to constitutional event sourcing requirement
- Quantity change events: More complex, less clear business semantics

### 2. Concurrent Operation Resolution

**Decision**: Implement eventstream version checks using EventVersion  
**Rationale**: Provides optimistic concurrency control. Prevents lost updates while maintaining performance. Aligns with existing domain/events/EventVersion.kt pattern.  
**Alternatives considered**:
- Pessimistic locking: Would violate performance targets
- Last-writer-wins: Could cause data loss
- Event ordering: Too complex for current scope

### 3. Validation Error Handling

**Decision**: Create domain-specific exception types extending DomainException  
**Rationale**: Follows existing patterns in domain layer. Provides type-safe error handling. Enables different error responses for different validation failures.  
**Alternatives considered**:
- Result pattern: Not established in current codebase
- Status codes: Too infrastructure-focused for domain layer

### 4. Performance Optimization Strategy

**Decision**: Use in-memory projections for cart state queries  
**Rationale**: In-memory operations easily meet 100ms target. Avoids premature optimization. Follows constitutional in-memory priority.  
**Alternatives considered**:
- Database indices: Violates in-memory implementation requirement
- Caching layers: Unnecessary complexity for current scale

### 5. Command Pattern Integration  

**Decision**: Extend existing command infrastructure with RemoveItemCommand  
**Rationale**: Maintains consistency with existing AddItemCommand pattern. Leverages established command validation and routing.  
**Alternatives considered**:
- Direct method calls: Would bypass existing command infrastructure
- Generic command pattern: Less type-safe than specific command classes

## Technical Dependencies

### Existing Domain Patterns
- Cart aggregate: `domain/src/main/kotlin/cart/`
- Command infrastructure: `domain/src/main/kotlin/commands/`  
- Event handling: `domain/src/main/kotlin/events/`
- Event versioning: `EventVersion` value class

### Required Extensions
- RemoveItemCommand: New command class
- ItemRemovedEvent: New domain event
- Cart.removeItem(): New aggregate method
- Command validation: Extend existing patterns

## Performance Considerations

### Response Time Target: <100ms for 95% of operations

**In-Memory Operations**: ~1-10μs per operation  
**Event Append**: ~10-100μs using existing EventStore  
**Projection Update**: ~1-10μs for cart state  
**Total Estimated**: ~50μs typical case, well under 100ms target

### Concurrency Handling
- EventVersion checks add ~10μs overhead
- Retry logic for conflicts adds variable latency
- Expected conflict rate: <1% for typical usage patterns

## Integration Points

### Existing Command Flow
1. Command validation → 2. Aggregate method → 3. Event generation → 4. Event store append → 5. Projection update

### Remove Item Flow  
1. RemoveItemCommand validation (product ID exists)
2. Cart.removeItem(productId, version) 
3. ItemRemovedEvent generation
4. EventStore.append with version check
5. Cart projection update

## Risk Assessment

### Technical Risks
- **Low**: Event versioning conflicts - established pattern
- **Low**: Performance targets - in-memory operations well under limits  
- **Low**: Integration complexity - follows existing patterns

### Mitigation Strategies
- Comprehensive unit tests for edge cases
- Integration tests for concurrent scenarios  
- Performance benchmarks for regression detection
