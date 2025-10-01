# Tasks: Remove Item from Cart

**Input**: Design documents from `/workspaces/kotlin-experiment/specs/002-removing-an-item/`
**Prerequisites**: plan.md ✓, research.md ✓, data-model.md ✓, contracts/ ✓

## Execution Flow (main)
```
1. Load plan.md from feature directory
   → Tech stack: Kotlin 1.9.25, Gradle, JUnit Platform
   → Structure: Multi-module (domain, cli-app, http-app, infrastructure)
2. Load design documents:
   → data-model.md: RemoveItemCommand, ItemRemovedEvent, Cart extensions
   → contracts/: CartService, CLI, HTTP API contracts  
   → research.md: Event sourcing patterns, concurrency control
3. Generate tasks by category:
   → Setup: None needed (existing project)
   → Tests: Domain, service, CLI, HTTP contract tests
   → Core: Domain entities, service implementation 
   → Integration: CLI and HTTP implementations
   → Polish: Performance tests, edge case validation
4. Apply task rules:
   → Different files = mark [P] for parallel
   → Same file = sequential (no [P])
   → Tests before implementation (TDD)
5. Number tasks sequentially (T001, T002...)
6. Generate dependency graph
7. Create parallel execution examples
8. Validate task completeness: All contracts have tests, all entities implemented
9. Return: SUCCESS (tasks ready for execution)
```

## Format: `[ID] [P?] Description`
- **[P]**: Can run in parallel (different files, no dependencies)
- Include exact file paths in descriptions

## Path Conventions
Multi-module Gradle project structure:
- **Domain**: `domain/src/main/kotlin/`, `domain/src/test/kotlin/`
- **CLI**: `cli-app/src/main/kotlin/`, `cli-app/src/test/kotlin/`
- **HTTP**: `http-app/src/main/kotlin/`, `http-app/src/test/kotlin/`
- **Infrastructure**: `infrastructure/src/main/kotlin/`, `infrastructure/src/test/kotlin/`

## Phase 3.1: Setup
- [x] T001 Project structure already exists per constitutional requirements
- [x] T002 Kotlin dependencies already configured per existing modules

## Phase 3.2: Tests First (TDD) ⚠️ MUST COMPLETE BEFORE 3.3
**CRITICAL: These tests MUST be written and MUST FAIL before ANY implementation**

### Domain Layer Tests
- [x] T003 [P] Test RemoveItemCommand validation in `domain/src/test/kotlin/commands/RemoveItemCommandTest.kt`
- [x] T004 [P] Test ItemRemovedEvent creation in `domain/src/test/kotlin/events/ItemRemovedEventTest.kt` 
- [x] T005 [P] Test ItemNotInCartException behavior in `domain/src/test/kotlin/cart/ItemNotInCartExceptionTest.kt`
- [x] T006 [P] Test EmptyCartException behavior in `domain/src/test/kotlin/cart/EmptyCartExceptionTest.kt`
- [x] T007 Test Cart.removeItem() method in `domain/src/test/kotlin/cart/CartTest.kt` (extends existing)
- [x] T008 Test CartService.removeItem() contract in `domain/src/test/kotlin/services/CartServiceTest.kt` (extends existing)

### CLI Layer Tests  
- [ ] T009 [P] Test CLI remove command parsing in `cli-app/src/test/kotlin/commands/RemoveCommandTest.kt`
- [ ] T010 [P] Test CLI remove success output in `cli-app/src/test/kotlin/output/RemoveOutputTest.kt`
- [ ] T011 [P] Test CLI remove error handling in `cli-app/src/test/kotlin/errors/RemoveErrorTest.kt`

### HTTP Layer Tests
- [ ] T012 [P] Test POST /carts/{id}/remove-item success response in `http-app/src/test/kotlin/controllers/RemoveItemControllerTest.kt`
- [ ] T013 [P] Test HTTP error responses (400, 409) in `http-app/src/test/kotlin/errors/RemoveItemErrorTest.kt`

### Integration Tests
- [ ] T014 [P] Test end-to-end remove item scenario with <100ms performance validation in `cli-app/src/test/kotlin/integration/RemoveItemIntegrationTest.kt`
- [ ] T015 [P] Test concurrent remove operations in `infrastructure/src/test/kotlin/concurrency/ConcurrentRemoveTest.kt`

## Phase 3.3: Core Implementation (ONLY after tests are failing)

### Domain Layer Implementation
- [x] T016 [P] Implement RemoveItemCommand in `domain/src/main/kotlin/commands/RemoveItemCommand.kt`
- [x] T017 [P] Implement ItemRemovedEvent in `domain/src/main/kotlin/events/ItemRemovedEvent.kt`
- [x] T018 [P] Implement ItemNotInCartException in `domain/src/main/kotlin/cart/ItemNotInCartException.kt`
- [x] T019 [P] Implement EmptyCartException in `domain/src/main/kotlin/cart/EmptyCartException.kt`
- [x] T020 Extend Cart.removeItem() method in `domain/src/main/kotlin/cart/Cart.kt`
- [x] T021 Extend CartService.removeItem() in `domain/src/main/kotlin/services/CartService.kt`

### CLI Layer Implementation
- [x] T022 Implement CLI remove command in `cli-app/src/main/kotlin/commands/RemoveCommand.kt`
- [x] T023 Implement CLI remove output formatting in `cli-app/src/main/kotlin/output/RemoveOutputFormatter.kt`
- [x] T024 Integrate remove command in CLI main in `cli-app/src/main/kotlin/Main.kt`

### HTTP Layer Implementation  
- [ ] T025 Implement POST /carts/{id}/remove-item controller in `http-app/src/main/kotlin/controllers/RemoveItemController.kt`
- [ ] T026 Implement HTTP error mapping in `http-app/src/main/kotlin/errors/RemoveItemErrorHandler.kt`
- [ ] T027 Register remove item routes in `http-app/src/main/kotlin/routing/CartRoutes.kt`

## Phase 3.4: Integration
- [x] T028 Wire CartService in CLI application in `cli-app/src/main/kotlin/di/ServiceModule.kt`
- [x] T029 Wire CartService in HTTP application in `http-app/src/main/kotlin/di/ServiceModule.kt`
- [x] T030 Add remove item event handling in `infrastructure/src/main/kotlin/eventstore/EventProcessor.kt`

## Phase 3.5: Polish
- [ ] T031 [P] Performance test: <100ms response time in `infrastructure/src/test/kotlin/performance/RemoveItemPerformanceTest.kt`
- [ ] T032 [P] Edge case test: Remove from cart with single item in `domain/src/test/kotlin/cart/SingleItemRemovalTest.kt`
- [ ] T033 [P] Edge case test: Multiple rapid removals in `domain/src/test/kotlin/cart/RapidRemovalTest.kt`
- [ ] T034 Validate quickstart scenarios work end-to-end (all 5 scenarios pass with correct exit codes and output)
- [ ] T035 Remove any code duplication in domain layer
- [ ] T036 Verify all constitutional requirements met

## Dependencies

**Phase Order**: Tests (T003-T015) → Implementation (T016-T027) → Integration (T028-T030) → Polish (T031-T036)

**Domain Dependencies**:
- T020 (Cart.removeItem) requires T016-T019 (commands, events, exceptions)
- T021 (CartService) requires T020 (Cart method)

**Application Dependencies**:
- T022-T024 (CLI) requires T021 (CartService)
- T025-T027 (HTTP) requires T021 (CartService)  
- T028-T030 (Integration) requires all implementation tasks

**Test Dependencies**:
- T007 (Cart tests) may need updates after T020 implementation
- T008 (Service tests) may need updates after T021 implementation

## Parallel Execution Examples

### Domain Tests (Phase 3.2 Start)
```bash
# Launch T003-T006 together:
Task: "Test RemoveItemCommand validation in domain/src/test/kotlin/commands/RemoveItemCommandTest.kt"
Task: "Test ItemRemovedEvent creation in domain/src/test/kotlin/events/ItemRemovedEventTest.kt" 
Task: "Test ItemNotInCartException behavior in domain/src/test/kotlin/cart/ItemNotInCartExceptionTest.kt"
Task: "Test EmptyCartException behavior in domain/src/test/kotlin/cart/EmptyCartExceptionTest.kt"
```

### CLI Tests
```bash
# Launch T009-T011 together:
Task: "Test CLI remove command parsing in cli-app/src/test/kotlin/commands/RemoveCommandTest.kt"
Task: "Test CLI remove success output in cli-app/src/test/kotlin/output/RemoveOutputTest.kt"
Task: "Test CLI remove error handling in cli-app/src/test/kotlin/errors/RemoveErrorTest.kt"
```

### Domain Implementation  
```bash
# Launch T016-T019 together:
Task: "Implement RemoveItemCommand in domain/src/main/kotlin/commands/RemoveItemCommand.kt"
Task: "Implement ItemRemovedEvent in domain/src/main/kotlin/events/ItemRemovedEvent.kt"
Task: "Implement ItemNotInCartException in domain/src/main/kotlin/cart/ItemNotInCartException.kt"
Task: "Implement EmptyCartException in domain/src/main/kotlin/cart/EmptyCartException.kt"
```

### Performance Tests
```bash
# Launch T031-T033 together:
Task: "Performance test: <100ms response time in infrastructure/src/test/kotlin/performance/RemoveItemPerformanceTest.kt"
Task: "Edge case test: Remove from cart with single item in domain/src/test/kotlin/cart/SingleItemRemovalTest.kt"
Task: "Edge case test: Multiple rapid removals in domain/src/test/kotlin/cart/RapidRemovalTest.kt"
```

## Notes
- All tests MUST fail initially (Red phase of TDD)
- Follow Red → Green → Refactor cycle for each task
- Commit after each task completion
- Verify constitutional compliance (domain purity, event sourcing, etc.)
- Use existing patterns from 001-add-items-to feature as reference
- EventVersion-based concurrency control is critical for T015, T030

## Validation Checklist
- [ ] All contracts have corresponding tests
- [ ] All domain entities have implementations  
- [ ] CLI interface matches contract specification
- [ ] HTTP API matches OpenAPI specification
- [ ] Performance requirements met (<100ms for 95% operations)
- [ ] Concurrent operations handled correctly
- [ ] Error scenarios properly tested
- [ ] Integration tests cover quickstart scenarios
