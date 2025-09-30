
# Implementation Plan: Add Items to Cart

**Branch**: `001-add-items-to` | **Date**: September 30, 2025 | **Spec**: /workspaces/kotlin-experiment/specs/001-add-items-to/spec.md
**Input**: Feature specification from `/specs/001-add-items-to/spec.md`

## Execution Flow (/plan command scope)
```
1. Load feature spec from Input path
   → If not found: ERROR "No feature spec at {path}"
2. Fill Technical Context (scan for NEEDS CLARIFICATION)
   → Detect Project Type from file system structure or context (web=frontend+backend, mobile=app+api)
   → Set Structure Decision based on project type
3. Fill the Constitution Check section based on the content of the constitution document.
4. Evaluate Constitution Check section below
   → If violations exist: Document in Complexity Tracking
   → If no justification possible: ERROR "Simplify approach first"
   → Update Progress Tracking: Initial Constitution Check
5. Execute Phase 0 → research.md
   → If NEEDS CLARIFICATION remain: ERROR "Resolve unknowns"
6. Execute Phase 1 → contracts, data-model.md, quickstart.md, agent-specific template file (e.g., `CLAUDE.md` for Claude Code, `.github/copilot-instructions.md` for GitHub Copilot, `GEMINI.md` for Gemini CLI, `QWEN.md` for Qwen Code or `AGENTS.md` for opencode).
7. Re-evaluate Constitution Check section
   → If new violations: Refactor design, return to Phase 1
   → Update Progress Tracking: Post-Design Constitution Check
8. Plan Phase 2 → Describe task generation approach (DO NOT create tasks.md)
9. STOP - Ready for /tasks command
```

**IMPORTANT**: The /plan command STOPS at step 7. Phases 2-4 are executed by other commands:
- Phase 2: /tasks command creates tasks.md
- Phase 3-4: Implementation execution (manual or via tools)

## Summary
Implement shopping cart functionality with item addition, quantity management, and capacity constraints using event-driven architecture. Cart operations will be modeled as domain events with optimistic locking for concurrency control.

## Technical Context
**Language/Version**: Kotlin (targeting JVM, multiplatform capability where beneficial)  
**Primary Dependencies**: Kotlin stdlib only for domain layer, Ktor for HTTP server, Gradle for build management  
**Storage**: In-memory Event Store implementation (per constitutional priority)  
**Testing**: Kotlin Test framework, TDD approach with Red→Green→Refactor cycles  
**Target Platform**: JVM/JRE (Linux development environment, cross-platform capability)
**Project Type**: Single domain project with separated modules (domain/cli-app/http-app/infrastructure)  
**Performance Goals**: In-memory event store operations, optimize for clarity over performance initially  
**Constraints**: Domain purity (no external dependencies), event sourcing architecture, 90%+ test coverage  
**Scale/Scope**: Single cart operations, 3-item limit, foundation for larger e-commerce domain

## Constitution Check
*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

**Domain-Driven Design**: ✅ Cart and Item are clear domain entities with business-driven boundaries  
**Event Modeling**: ✅ Cart operations (add item, create cart, validate capacity) map to domain events  
**Test-First Development**: ✅ TDD cycle planned with failing tests before implementation  
**Architectural Separation**: ✅ Domain layer will be pure Kotlin, separate from CLI/HTTP applications  
**In-Memory Implementation**: ✅ Starting with in-memory EventStore as constitutional priority  

**Module Structure**: ✅ Separate gradle modules planned: domain, cli-app, http-app, infrastructure  
**Technology Stack**: ✅ Pure Kotlin domain layer, no external framework dependencies  
**Event Store Interface**: ✅ Single EventStore abstraction for append/read/subscribe operations  

**Violations**: None identified - plan aligns with all constitutional requirements

## Project Structure

### Documentation (this feature)
```
specs/[###-feature]/
├── plan.md              # This file (/plan command output)
├── research.md          # Phase 0 output (/plan command)
├── data-model.md        # Phase 1 output (/plan command)
├── quickstart.md        # Phase 1 output (/plan command)
├── contracts/           # Phase 1 output (/plan command)
└── tasks.md             # Phase 2 output (/tasks command - NOT created by /plan)
```

### Source Code (repository root)
```
# Kotlin Event Store Modular Architecture
domain/
├── src/
│   ├── main/kotlin/
│   │   ├── cart/           # Cart aggregate root and domain services
│   │   ├── item/           # Item value objects and domain logic  
│   │   ├── events/         # Domain events for cart operations
│   │   └── eventstore/     # EventStore abstraction and interfaces
│   └── test/kotlin/
│       ├── cart/           # Domain unit tests
│       ├── item/           # Value object tests
│       └── events/         # Event serialization tests

cli-app/
├── src/
│   ├── main/kotlin/
│   │   ├── commands/       # CLI command handlers
│   │   └── Main.kt         # CLI application entry point
│   └── test/kotlin/
│       └── integration/    # End-to-end CLI tests

http-app/
├── src/
│   ├── main/kotlin/
│   │   ├── handlers/       # HTTP request handlers  
│   │   └── Server.kt       # HTTP server setup
│   └── test/kotlin/
│       └── api/            # API integration tests

infrastructure/
├── src/
│   ├── main/kotlin/
│   │   ├── eventstore/     # In-memory EventStore implementation
│   │   └── persistence/    # Future: file/database adapters
│   └── test/kotlin/
│       └── eventstore/     # Infrastructure tests

build.gradle.kts            # Root build configuration
settings.gradle.kts         # Multi-module project setup
```

**Structure Decision**: Selected Kotlin multi-module architecture per constitutional requirements. Domain module is dependency-free, application modules depend on domain + infrastructure, clear separation enforced via Gradle constraints.

## Phase 0: Outline & Research
1. **Extract unknowns from Technical Context** above:
   - For each NEEDS CLARIFICATION → research task
   - For each dependency → best practices task
   - For each integration → patterns task

2. **Generate and dispatch research agents**:
   ```
   For each unknown in Technical Context:
     Task: "Research {unknown} for {feature context}"
   For each technology choice:
     Task: "Find best practices for {tech} in {domain}"
   ```

3. **Consolidate findings** in `research.md` using format:
   - Decision: [what was chosen]
   - Rationale: [why chosen]
   - Alternatives considered: [what else evaluated]

**Output**: research.md with all NEEDS CLARIFICATION resolved

## Phase 1: Design & Contracts
*Prerequisites: research.md complete*

1. **Extract entities from feature spec** → `data-model.md`:
   - Entity name, fields, relationships
   - Validation rules from requirements
   - State transitions if applicable

2. **Generate API contracts** from functional requirements:
   - For each user action → endpoint
   - Use standard REST/GraphQL patterns
   - Output OpenAPI/GraphQL schema to `/contracts/`

3. **Generate contract tests** from contracts:
   - One test file per endpoint
   - Assert request/response schemas
   - Tests must fail (no implementation yet)

4. **Extract test scenarios** from user stories:
   - Each story → integration test scenario
   - Quickstart test = story validation steps

5. **Update agent file incrementally** (O(1) operation):
   - Run `.specify/scripts/bash/update-agent-context.sh copilot`
     **IMPORTANT**: Execute it exactly as specified above. Do not add or remove any arguments.
   - If exists: Add only NEW tech from current plan
   - Preserve manual additions between markers
   - Update recent changes (keep last 3)
   - Keep under 150 lines for token efficiency
   - Output to repository root

**Output**: data-model.md, /contracts/*, failing tests, quickstart.md, agent-specific file

## Phase 2: Task Planning Approach
*This section describes what the /tasks command will do - DO NOT execute during /plan*

**Task Generation Strategy**:
- Load `.specify/templates/tasks-template.md` as base
- Generate tasks from Phase 1 design docs (data-model.md, contracts/, quickstart.md)
- Domain entities → failing unit test tasks followed by implementation tasks [P]
- Value objects → test + implementation task pairs [P]
- Domain events → serialization test + implementation tasks [P]
- Each acceptance scenario from quickstart.md → integration test task
- EventStore interface → contract test + in-memory implementation tasks
- CartService → domain service test + implementation tasks  
- CLI commands → end-to-end test + implementation tasks
- HTTP endpoints → API test + implementation tasks

**Ordering Strategy**:
- TDD order: All tests written before any implementation
- Constitutional order: Domain → Infrastructure → Applications
- Dependency order: Value Objects → Entities → Services → Applications  
- Gradle module order: domain → infrastructure → cli-app → http-app
- Mark [P] for parallel execution within same layer

**Specific Task Categories**:
1. **Foundation Tasks**: Gradle setup, module configuration, basic project structure
2. **Domain Layer Tests**: Value object tests, entity tests, event tests, service contract tests  
3. **Domain Layer Implementation**: Make domain tests pass while maintaining purity
4. **Infrastructure Tests**: EventStore contract tests, repository tests
5. **Infrastructure Implementation**: In-memory EventStore, concrete repositories
6. **CLI Application Tests**: Command parsing tests, integration tests from quickstart scenarios
7. **CLI Application Implementation**: Command handlers, main application logic
8. **HTTP Application Tests**: API endpoint tests, contract validation tests
9. **HTTP Application Implementation**: Route handlers, server setup
10. **Integration Validation**: End-to-end acceptance tests, performance validation

**Estimated Output**: 35-40 numbered, ordered tasks in tasks.md following constitutional TDD principles

**IMPORTANT**: This phase is executed by the /tasks command, NOT by /plan

## Phase 3+: Future Implementation
*These phases are beyond the scope of the /plan command*

**Phase 3**: Task execution (/tasks command creates tasks.md)  
**Phase 4**: Implementation (execute tasks.md following constitutional principles)  
**Phase 5**: Validation (run tests, execute quickstart.md, performance validation)

## Complexity Tracking
*Fill ONLY if Constitution Check has violations that must be justified*

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |


## Progress Tracking
*This checklist is updated during execution flow*

**Phase Status**:
- [x] Phase 0: Research complete (/plan command)
- [x] Phase 1: Design complete (/plan command)
- [x] Phase 2: Task planning complete (/plan command - describe approach only)
- [ ] Phase 3: Tasks generated (/tasks command)
- [ ] Phase 4: Implementation complete
- [ ] Phase 5: Validation passed

**Gate Status**:
- [x] Initial Constitution Check: PASS
- [x] Post-Design Constitution Check: PASS  
- [x] All NEEDS CLARIFICATION resolved
- [x] Complexity deviations documented (none required)

---
*Based on Constitution v2.1.1 - See `/memory/constitution.md`*
