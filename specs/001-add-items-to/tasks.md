# Tasks: Add Items to Cart

**Input**: Design documents from `/specs/001-add-items-to/`
**Prerequisites**: plan.md (required), research.md, data-model.md, contracts/

## Execution Flow (main)
```
1. Load plan.md from feature directory
   → ✓ Found: Kotlin Event Store multi-module architecture
   → ✓ Tech stack: Kotlin JVM, Gradle, in-memory EventStore
2. Load optional design documents:
   → ✓ data-model.md: Cart, LineItem, ProductId, CartId, Events
   → ✓ contracts/: HTTP API and CLI specifications
   → ✓ research.md: Event sourcing decisions, optimistic locking
   → ✓ quickstart.md: TDD acceptance scenarios
3. Generate tasks by category:
   → Setup: Gradle multi-module, dependencies, structure
   → Tests: Domain tests, contract tests, integration tests
   → Core: Value objects, entities, events, services
   → Applications: CLI commands, HTTP endpoints
   → Infrastructure: EventStore, repositories
   → Polish: Performance tests, documentation
4. Apply task rules:
   → Different modules/files = [P] parallel
   → Tests before implementation (TDD)
   → Domain → Infrastructure → Applications
5. Number tasks sequentially (T001-T040)
6. Generate dependency graph with constitutional TDD approach
7. SUCCESS: 40 tasks ready for execution
```

## Format: `[ID] [P?] Description`
- **[P]**: Can run in parallel (different files/modules, no dependencies)  
- Include exact file paths following multi-module structure

## Path Conventions (Kotlin Multi-Module)
```
domain/src/main/kotlin/           # Pure domain logic
domain/src/test/kotlin/           # Domain unit tests
infrastructure/src/main/kotlin/   # EventStore implementation
infrastructure/src/test/kotlin/   # Infrastructure tests
cli-app/src/main/kotlin/          # CLI application
cli-app/src/test/kotlin/          # CLI integration tests
http-app/src/main/kotlin/         # HTTP API application  
http-app/src/test/kotlin/         # HTTP API tests
```

## Phase 3.1: Project Setup
- [ ] T001 Initialize Gradle multi-module project with settings.gradle.kts and root build.gradle.kts
- [ ] T002 [P] Configure domain module build.gradle.kts with Kotlin stdlib only
- [ ] T003 [P] Configure infrastructure module build.gradle.kts depending on domain
- [ ] T004 [P] Configure cli-app module build.gradle.kts depending on domain and infrastructure
- [ ] T005 [P] Configure http-app module build.gradle.kts depending on domain and infrastructure
- [ ] T006 [P] Setup Kotlin test framework and assertion library dependencies
- [ ] T007 [P] Configure linting with ktlint and code formatting

## Phase 3.2: Domain Tests First (TDD) ⚠️ MUST COMPLETE BEFORE 3.3
**CRITICAL: These tests MUST be written and MUST FAIL before ANY domain implementation**
- [ ] T008 [P] Value object tests in domain/src/test/kotlin/cart/ProductIdTest.kt
- [ ] T009 [P] Value object tests in domain/src/test/kotlin/cart/CartIdTest.kt  
- [ ] T010 [P] Value object tests in domain/src/test/kotlin/cart/EventVersionTest.kt
- [ ] T011 [P] LineItem entity tests in domain/src/test/kotlin/cart/LineItemTest.kt
- [ ] T012 [P] Cart aggregate tests in domain/src/test/kotlin/cart/CartTest.kt
- [ ] T013 [P] Domain event tests in domain/src/test/kotlin/events/CartEventsTest.kt
- [ ] T014 [P] AddItemToCart command tests in domain/src/test/kotlin/commands/AddItemToCartTest.kt
- [ ] T015 Cart acceptance scenario tests in domain/src/test/kotlin/cart/CartAcceptanceTest.kt

## Phase 3.3: Domain Implementation (ONLY after tests are failing)
- [ ] T016 [P] ProductId value class in domain/src/main/kotlin/cart/ProductId.kt
- [ ] T017 [P] CartId value class in domain/src/main/kotlin/cart/CartId.kt
- [ ] T018 [P] EventVersion value class in domain/src/main/kotlin/events/EventVersion.kt
- [ ] T019 [P] LineItem entity in domain/src/main/kotlin/cart/LineItem.kt
- [ ] T020 Cart aggregate root in domain/src/main/kotlin/cart/Cart.kt
- [ ] T021 [P] DomainEvent interface in domain/src/main/kotlin/events/DomainEvent.kt
- [ ] T022 [P] CartCreated event in domain/src/main/kotlin/events/CartCreated.kt
- [ ] T023 [P] ItemAddedToCart event in domain/src/main/kotlin/events/ItemAddedToCart.kt
- [ ] T024 [P] AddItemToCart command in domain/src/main/kotlin/commands/AddItemToCart.kt
- [ ] T025 Domain exceptions in domain/src/main/kotlin/exceptions/CartExceptions.kt

## Phase 3.4: Infrastructure Tests First
**CRITICAL: Infrastructure tests before implementation**
- [ ] T026 [P] EventStore interface contract tests in infrastructure/src/test/kotlin/eventstore/EventStoreContractTest.kt
- [ ] T027 [P] InMemoryEventStore tests in infrastructure/src/test/kotlin/eventstore/InMemoryEventStoreTest.kt  
- [ ] T028 [P] CartRepository tests in infrastructure/src/test/kotlin/repositories/CartRepositoryTest.kt

## Phase 3.5: Infrastructure Implementation  
- [ ] T029 EventStore interface in domain/src/main/kotlin/eventstore/EventStore.kt
- [ ] T030 [P] EventEnvelope data class in infrastructure/src/main/kotlin/eventstore/EventEnvelope.kt
- [ ] T031 InMemoryEventStore implementation in infrastructure/src/main/kotlin/eventstore/InMemoryEventStore.kt
- [ ] T032 [P] CartRepository interface in domain/src/main/kotlin/repositories/CartRepository.kt
- [ ] T033 CartRepositoryImpl in infrastructure/src/main/kotlin/repositories/CartRepositoryImpl.kt

## Phase 3.6: Domain Service Implementation
- [ ] T034 CartService interface in domain/src/main/kotlin/services/CartService.kt
- [ ] T035 CartService implementation with event sourcing logic in infrastructure/src/main/kotlin/services/CartServiceImpl.kt

## Phase 3.7: CLI Application Tests First
- [ ] T036 [P] CLI command parsing tests in cli-app/src/test/kotlin/commands/AddItemCommandTest.kt
- [ ] T037 [P] CLI integration tests in cli-app/src/test/kotlin/integration/CliIntegrationTest.kt

## Phase 3.8: CLI Application Implementation
- [ ] T038 AddItemCommand handler in cli-app/src/main/kotlin/commands/AddItemCommand.kt
- [ ] T039 CLI main application in cli-app/src/main/kotlin/Main.kt

## Phase 3.9: HTTP API Tests First  
- [ ] T040 [P] HTTP API contract tests in http-app/src/test/kotlin/api/CartApiContractTest.kt
- [ ] T041 [P] HTTP API integration tests in http-app/src/test/kotlin/integration/ApiIntegrationTest.kt

## Phase 3.10: HTTP API Implementation
- [ ] T042 POST /carts/{cartId}/items endpoint in http-app/src/main/kotlin/handlers/AddItemHandler.kt
- [ ] T043 POST /carts/items endpoint in http-app/src/main/kotlin/handlers/CreateCartHandler.kt  
- [ ] T044 GET /carts/{cartId} endpoint in http-app/src/main/kotlin/handlers/GetCartHandler.kt
- [ ] T045 HTTP server setup and routing in http-app/src/main/kotlin/Server.kt

## Phase 3.11: Integration & Polish
- [ ] T046 [P] End-to-end acceptance validation tests in tests/acceptance/
- [ ] T047 [P] Performance benchmarks for EventStore operations in tests/performance/
- [ ] T048 [P] Update project README.md with quickstart instructions
- [ ] T049 [P] Generate API documentation from contracts
- [ ] T050 Final constitutional compliance validation and cleanup

## Dependencies
**Critical Path (must be sequential)**:
- Setup (T001-T007) → Domain Tests (T008-T015) → Domain Implementation (T016-T025)
- Domain complete → Infrastructure Tests (T026-T028) → Infrastructure (T029-T035)
- Core complete → Application Tests (T036-T037, T040-T041) → Applications (T038-T039, T042-T045)
- All implementation → Polish (T046-T050)

**Blocking Dependencies**:
- T020 (Cart) depends on T016-T019 (value objects, LineItem)
- T031 (InMemoryEventStore) depends on T029 (EventStore interface)  
- T035 (CartServiceImpl) depends on T029, T032, T034
- T038-T039 (CLI) depend on T035 (CartService)
- T042-T045 (HTTP) depend on T035 (CartService)

## Parallel Execution Examples

### Setup Phase (can run together):
```bash
# T002-T005: Module configurations
Task: "Configure domain module build.gradle.kts with Kotlin stdlib only"
Task: "Configure infrastructure module build.gradle.kts depending on domain"  
Task: "Configure cli-app module build.gradle.kts depending on domain and infrastructure"
Task: "Configure http-app module build.gradle.kts depending on domain and infrastructure"
```

### Domain Tests Phase (can run together):
```bash
# T008-T014: Different test files  
Task: "Value object tests in domain/src/test/kotlin/cart/ProductIdTest.kt"
Task: "Value object tests in domain/src/test/kotlin/cart/CartIdTest.kt"
Task: "LineItem entity tests in domain/src/test/kotlin/cart/LineItemTest.kt"
Task: "Domain event tests in domain/src/test/kotlin/events/CartEventsTest.kt"
```

### Domain Implementation Phase (can run together):
```bash
# T016-T019, T021-T024: Different files
Task: "ProductId value class in domain/src/main/kotlin/cart/ProductId.kt"
Task: "CartId value class in domain/src/main/kotlin/cart/CartId.kt"
Task: "DomainEvent interface in domain/src/main/kotlin/events/DomainEvent.kt"
Task: "CartCreated event in domain/src/main/kotlin/events/CartCreated.kt"
```

## TDD Validation Checklist
*GATE: Must verify before proceeding to implementation*

- [ ] All domain tests (T008-T015) written and FAILING
- [ ] All infrastructure tests (T026-T028) written and FAILING  
- [ ] All application tests (T036-T037, T040-T041) written and FAILING
- [ ] No implementation code exists when tests are written
- [ ] Each test validates specific acceptance criteria from quickstart.md
- [ ] Tests cover constitutional requirements (domain purity, event sourcing)

## Constitutional Compliance Notes
- **Domain Purity**: Tasks T016-T025 must have zero external dependencies
- **Event Sourcing**: All state changes through events (T021-T023, T031)
- **TDD Approach**: All test tasks before corresponding implementation tasks
- **Architectural Separation**: Clear module boundaries enforced through dependencies
- **In-Memory Priority**: EventStore starts as in-memory implementation (T031)

## Notes
- [P] tasks target different files/modules with no shared dependencies
- Verify all tests FAIL before implementing (Red phase of TDD)
- Commit after each task completion
- Follow constitutional principles: domain-first, event-driven, test-driven
- Module dependency order: domain → infrastructure → applications
