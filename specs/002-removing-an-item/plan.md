
# Implementation Plan: Remove Item from Cart

**Branch**: `002-removing-an-item` | **Date**: September 30, 2025 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/workspaces/kotlin-experiment/specs/002-removing-an-item/spec.md`

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
Enable users to remove items from their cart by product ID, reducing quantity by 1 per operation. When quantity reaches 0, remove item entirely. Return validation errors for non-existent items. Use event sourcing with eventstream version checks to handle concurrent operations. Target 95% of operations under 100ms response time.

## Technical Context
**Language/Version**: Kotlin 1.9.25 (JVM target, multiplatform capability where beneficial)  
**Primary Dependencies**: Kotlin stdlib only for domain layer, Gradle for build management  
**Storage**: In-memory EventStore implementation (constitutional requirement)  
**Testing**: Kotlin Test with JUnit Platform  
**Target Platform**: JVM (Linux development environment)
**Project Type**: Multi-module Gradle project (domain, cli-app, http-app, infrastructure)  
**Performance Goals**: Under 100ms response time for 95% of remove operations  
**Constraints**: Domain purity (no external dependencies), Event sourcing architecture, TDD workflow  
**Scale/Scope**: Cart operations for single-user sessions, event-driven state management

## Constitution Check
*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

**I. Domain-Driven Design**: ✅ PASS - Feature focused on Cart domain entity with clear business operations (remove item)  
**II. Event Modeling Architecture**: ✅ PASS - Remove operations will generate events, leverage existing event store  
**III. Test-First Development**: ✅ PASS - Will follow TDD cycle: Red → Green → Refactor  
**IV. Architectural Separation**: ✅ PASS - Using existing module structure (domain, cli-app, infrastructure)  
**V. In-Memory Implementation**: ✅ PASS - Leveraging existing in-memory event store implementation  

**Technology Stack**: ✅ PASS - Gradle Kotlin project, pure Kotlin for domain logic  
**Module Structure**: ✅ PASS - Domain module independent, clear dependency direction  
**Event Store Interface**: ✅ PASS - Will use existing EventStore abstraction for cart events

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
domain/
├── src/main/kotlin/
│   ├── cart/           # Cart aggregate and entities  
│   ├── commands/       # Command objects (RemoveItemCommand)
│   ├── events/         # Domain events (ItemRemovedEvent)
│   ├── eventstore/     # Event store abstractions
│   └── services/       # Domain services
└── src/test/kotlin/    # Domain unit tests

cli-app/
├── src/main/kotlin/    # CLI interface for cart operations
└── src/test/kotlin/    # CLI integration tests

http-app/
├── src/main/kotlin/    # HTTP API for cart operations  
└── src/test/kotlin/    # HTTP integration tests

infrastructure/
├── src/main/kotlin/    # In-memory implementations
└── src/test/kotlin/    # Infrastructure tests
```

**Structure Decision**: Multi-module Gradle project following constitutional separation. Domain contains pure business logic, applications provide interfaces, infrastructure provides implementations. Remove item functionality will extend existing cart domain model with new command/event pattern.

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
- Generate TDD tasks from Phase 1 artifacts:
  - Domain model tests: RemoveItemCommand, ItemRemovedEvent, Cart.removeItem() [P]
  - Exception tests: ItemNotInCartException, EmptyCartException [P]
  - Service contract tests: CartService.removeItem() validation
  - CLI contract tests: Command parsing, output formatting [P]
  - HTTP API contract tests: Request/response validation [P]
  - Integration tests: End-to-end remove item scenarios
  - Performance tests: 100ms response time validation

**Ordering Strategy**:
- Phase 1: Domain tests (exceptions, commands, events, aggregates)
- Phase 2: Service layer tests (domain services, event handling)  
- Phase 3: Application tests (CLI, HTTP API contracts)
- Phase 4: Implementation to make tests pass (follow TDD Red→Green→Refactor)
- Phase 5: Integration and performance validation

**Dependency Management**:
- Domain layer tasks are independent [P]
- Service tasks depend on domain completion
- Application tasks depend on service completion
- All tests written before implementation (constitutional TDD requirement)

**Estimated Output**: 20-25 numbered, ordered tasks following strict TDD methodology

**Performance Validation Tasks**:
- Response time measurement framework
- Concurrent operation testing infrastructure  
- Memory usage monitoring for event store operations

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
- [ ] Complexity deviations documented

---
*Based on Constitution v1.0.0 - See `.specify/memory/constitution.md`*
