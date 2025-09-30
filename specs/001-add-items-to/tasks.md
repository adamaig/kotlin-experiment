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
5. Number tasks sequentially (T001-T051)
6. Generate dependency graph with constitutional TDD approach
7. SUCCESS: 51 tasks ready for execution
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
- [x] T001 Initialize Gradle multi-module project with settings.gradle.kts and root build.gradle.kts
- [x] T002 [P] Configure domain module build.gradle.kts with Kotlin stdlib only
- [x] T003 [P] Configure infrastructure module build.gradle.kts depending on domain
- [x] T004 [P] Configure cli-app module build.gradle.kts depending on domain and infrastructure
- [x] T005 [P] Configure http-app module build.gradle.kts depending on domain and infrastructure
- [x] T006 [P] Setup Kotlin test framework and assertion library dependencies
- [ ] T007 [P] Configure linting with ktlint and code formatting

## Phase 3.2: Domain Tests First (TDD) ⚠️ MUST COMPLETE BEFORE 3.3
**CRITICAL: These tests MUST be written and MUST FAIL before ANY domain implementation**
- [x] T008 [P] Value object tests in domain/src/test/kotlin/cart/ProductIdTest.kt
- [x] T009 [P] Value object tests in domain/src/test/kotlin/cart/CartIdTest.kt  
- [x] T010 [P] Value object tests in domain/src/test/kotlin/cart/EventVersionTest.kt
- [x] T011 [P] LineItem entity tests in domain/src/test/kotlin/cart/LineItemTest.kt
- [x] T012 [P] Cart aggregate tests in domain/src/test/kotlin/cart/CartTest.kt
- [x] T013 [P] Domain event tests in domain/src/test/kotlin/events/CartEventsTest.kt
- [ ] T014 [P] AddItemToCart command tests in domain/src/test/kotlin/commands/AddItemToCartTest.kt
- [ ] T015 Cart acceptance scenario tests in domain/src/test/kotlin/cart/CartAcceptanceTest.kt

## Phase 3.3: Domain Implementation (ONLY after tests are failing)
- [x] T016 [P] ProductId value class in domain/src/main/kotlin/cart/ProductId.kt
- [x] T017 [P] CartId value class in domain/src/main/kotlin/cart/CartId.kt
- [x] T018 [P] EventVersion value class in domain/src/main/kotlin/events/EventVersion.kt
- [x] T019 [P] LineItem entity in domain/src/main/kotlin/cart/LineItem.kt
- [x] T020 Cart aggregate root in domain/src/main/kotlin/cart/Cart.kt
- [x] T021 [P] DomainEvent interface in domain/src/main/kotlin/events/DomainEvent.kt
- [x] T022 [P] CartCreated event in domain/src/main/kotlin/events/CartCreated.kt
- [x] T023 [P] ItemAddedToCart event in domain/src/main/kotlin/events/ItemAddedToCart.kt
- [x] T024 [P] AddItemToCart command in domain/src/main/kotlin/commands/AddItemToCart.kt
- [x] T025 Domain exceptions in domain/src/main/kotlin/exceptions/CartExceptions.kt

## Phase 3.4: Infrastructure Tests First
**CRITICAL: Infrastructure tests before implementation**
- [x] T026 [P] EventStore interface contract tests in infrastructure/src/test/kotlin/eventstore/EventStoreContractTest.kt
- [x] T027 [P] InMemoryEventStore tests in infrastructure/src/test/kotlin/eventstore/InMemoryEventStoreTest.kt  
- [x] T028 [P] CartRepository tests in infrastructure/src/test/kotlin/repositories/CartRepositoryTest.kt
- [x] T029 [P] Cart persistence validation tests in infrastructure/src/test/kotlin/repositories/CartPersistenceTest.kt

## Phase 3.5: Infrastructure Implementation  
- [x] T030 EventStore interface in domain/src/main/kotlin/eventstore/EventStore.kt
- [x] T031 [P] EventEnvelope data class in infrastructure/src/main/kotlin/eventstore/EventEnvelope.kt
- [x] T032 InMemoryEventStore implementation in infrastructure/src/main/kotlin/eventstore/InMemoryEventStore.kt
- [x] T033 [P] CartRepository interface in domain/src/main/kotlin/repositories/CartRepository.kt
- [x] T034 CartRepositoryImpl in infrastructure/src/main/kotlin/repositories/CartRepositoryImpl.kt

## Phase 3.6: Domain Service Implementation
- [x] T035 CartService interface in domain/src/main/kotlin/services/CartService.kt
- [x] T036 CartService implementation with event sourcing logic in infrastructure/src/main/kotlin/services/CartServiceImpl.kt

## Phase 3.7: CLI Application Tests First
- [x] T037 [P] CLI command parsing tests in cli-app/src/test/kotlin/commands/AddItemCommandTest.kt
- [x] T038 [P] CLI integration tests in cli-app/src/test/kotlin/integration/CliIntegrationTest.kt

## Phase 3.8: CLI Application Implementation
- [x] T039 AddItemCommand handler in cli-app/src/main/kotlin/commands/AddItemCommand.kt
- [x] T040 CLI main application in cli-app/src/main/kotlin/Main.kt

## Phase 3.9: HTTP API Tests First  
- [x] T041 [P] HTTP API contract tests in http-app/src/test/kotlin/api/CartApiContractTest.kt
- [x] T042 [P] HTTP API integration tests in http-app/src/test/kotlin/integration/ApiIntegrationTest.kt

## Phase 3.10: HTTP API Implementation
- [x] T043 POST /carts/{cartId}/items endpoint in http-app/src/main/kotlin/handlers/AddItemHandler.kt
- [x] T044 POST /carts/items endpoint in http-app/src/main/kotlin/handlers/CreateCartHandler.kt  
- [x] T045 GET /carts/{cartId} endpoint in http-app/src/main/kotlin/handlers/GetCartHandler.kt
- [x] T046 HTTP server setup and routing in http-app/src/main/kotlin/Server.kt

## Phase 3.11: Integration & Polish
- [x] T047 [P] End-to-end acceptance validation tests in tests/acceptance/
- [x] T048 [P] Performance benchmarks for EventStore operations in tests/performance/
- [x] T049 [P] Update project README.md with quickstart instructions
- [x] T050 [P] Generate API documentation from contracts
- [x] T051 Final constitutional compliance validation and cleanup

## Dependencies
**Critical Path (must be sequential)**:
- Setup (T001-T007) → Domain Tests (T008-T015) → Domain Implementation (T016-T025)
- Domain complete → Infrastructure Tests (T026-T029) → Infrastructure (T030-T036)
- Core complete → Application Tests (T037-T038, T041-T042) → Applications (T039-T040, T043-T046)
- All implementation → Polish (T047-T051)

**Blocking Dependencies**:
- T020 (Cart) depends on T016-T019 (value objects, LineItem)
- T032 (InMemoryEventStore) depends on T030 (EventStore interface)  
- T036 (CartServiceImpl) depends on T030, T033, T035
- T039-T040 (CLI) depend on T036 (CartService)
- T043-T046 (HTTP) depend on T036 (CartService)

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
- [ ] All infrastructure tests (T026-T029) written and FAILING  
- [ ] All application tests (T037-T038, T041-T042) written and FAILING
- [ ] No implementation code exists when tests are written
- [ ] Each test validates specific acceptance criteria from quickstart.md
- [ ] Tests cover constitutional requirements (domain purity, event sourcing)

## Constitutional Compliance Notes
- **Domain Purity**: Tasks T016-T025 must have zero external dependencies
- **Event Sourcing**: All state changes through events (T021-T023, T032)
- **TDD Approach**: All test tasks before corresponding implementation tasks
- **Architectural Separation**: Clear module boundaries enforced through dependencies
- **In-Memory Priority**: EventStore starts as in-memory implementation (T032)

## Notes
- [P] tasks target different files/modules with no shared dependencies
- Verify all tests FAIL before implementing (Red phase of TDD)
- Commit after each task completion
- Follow constitutional principles: domain-first, event-driven, test-driven
- Module dependency order: domain → infrastructure → applications
