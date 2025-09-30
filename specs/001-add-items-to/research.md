# Research: Add Items to Cart

## Technical Decisions

### Event Store Architecture
**Decision**: In-memory EventStore implementation using Kotlin sealed classes for events
**Rationale**: 
- Constitutional requirement for in-memory priority
- Sealed classes provide type-safe event polymorphism
- Enables complete audit trail of cart operations
- Supports temporal queries and event replay
**Alternatives considered**: Direct state mutation (rejected - not event-driven), external event store (rejected - adds complexity)

### Domain Event Design
**Decision**: Command-Event pattern with immutable events
**Rationale**: 
- Commands represent intent (AddItemToCart), events represent facts (ItemAddedToCart, CartCreated)
- Immutable events ensure audit integrity
- Clear separation of command validation from event application
**Alternatives considered**: CRUD-style operations (rejected - loses business intent), mutable events (rejected - breaks audit trail)

### Concurrency Control
**Decision**: Optimistic locking using event stream version numbers
**Rationale**: 
- Clarification specified optimistic locking approach
- Higher throughput than pessimistic locking
- Natural fit with event sourcing architecture
- Conflict detection through expected version validation
**Alternatives considered**: Pessimistic locking (rejected - lower performance), no concurrency control (rejected - data corruption risk)

### Item Identity Strategy  
**Decision**: Product ID as UUID or numeric identifier
**Rationale**: 
- Clarification specified Product ID approach
- Globally unique across system
- Enables duplicate detection for quantity increments
- Simple comparison for equality checks
**Alternatives considered**: SKU-based (rejected - not specified), composite keys (rejected - unnecessary complexity)

### Quantity Management
**Decision**: Single quantity field per cart line item
**Rationale**: 
- Clarification specified quantity increment behavior
- Simpler than separate line items for same product
- Natural aggregation for capacity calculations
- Efficient storage and processing
**Alternatives considered**: Separate line items (rejected - creates duplicates), quantity as events (rejected - overcomplicated)

### Capacity Validation
**Decision**: Total quantity sum validation with complete rejection
**Rationale**: 
- Clarification specified total quantity approach (sum ≤ 3)
- Clear business rule implementation
- Prevents partial additions that could confuse users
- Consistent with error message specification
**Alternatives considered**: Unique item count (rejected - clarified as total quantity), partial additions (rejected - clarified as complete rejection)

### Module Organization
**Decision**: Domain-first module structure with dependency constraints
**Rationale**: 
- Constitutional requirement for architectural separation
- Domain module has zero external dependencies
- Application modules orchestrate domain operations
- Infrastructure implements domain interfaces
**Alternatives considered**: Monolithic structure (rejected - violates constitution), feature-based modules (rejected - unclear boundaries)

## Domain Language Alignment

**Cart**: Aggregate root containing collection of line items with capacity constraints
**Item**: Value object identified by Product ID with associated quantity  
**Add Item**: Command that either creates new line item or increments existing quantity
**Capacity Limit**: Business constraint of maximum 3 total item quantities
**Line Item**: Cart entry with Product ID and quantity (internal concept)

## Implementation Strategy

**Phase 1**: Pure domain model with in-memory event store
**Phase 2**: CLI application for testing domain operations  
**Phase 3**: HTTP API for external integration
**Phase 4**: Infrastructure adapters for persistence (future)

All phases maintain constitutional compliance with TDD approach and architectural separation.
