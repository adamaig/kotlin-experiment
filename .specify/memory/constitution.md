<!-- 
Sync Impact Report:
- Version change: template → 1.0.0
- New constitution created for Kotlin Event Store Experiment
- Added principles: Domain-Driven Design, Event Modeling, Test-First Development, Architectural Separation, In-Memory Implementation
- Added sections: Architecture Constraints, Development Workflow  
- Templates requiring updates: ✅ plan-template.md compatible / ✅ spec-template.md compatible / ✅ tasks-template.md compatible
- Follow-up TODOs: None - all placeholders resolved
-->

# Kotlin Event Store Experiment Constitution

## Core Principles

### I. Domain-Driven Design (NON-NEGOTIABLE)
Domain models MUST be the central organizing principle of all code. Business logic resides exclusively in domain entities, value objects, and domain services. The domain layer MUST remain pure - no dependencies on infrastructure, frameworks, or I/O concerns. All domain concepts MUST use ubiquitous language derived from business requirements. Aggregate boundaries enforce consistency and encapsulation rules.

**Rationale**: DDD ensures the codebase reflects real business problems and remains maintainable as complexity grows. Pure domain models enable comprehensive testing and prevent architectural drift.

### II. Event Modeling Architecture  
All state changes MUST be expressed as immutable events stored in an event store. Commands generate events, events update read models, and queries read from projections. Events MUST be the single source of truth for all business state. Event schemas MUST be versioned and backward-compatible. No direct database mutations allowed outside the event store abstraction.

**Rationale**: Event sourcing provides complete audit trails, temporal queries, and natural separation between write and read concerns. Event modeling makes system behavior explicit and debuggable.

### III. Test-First Development (NON-NEGOTIABLE)
TDD cycle MUST be followed: Red → Green → Refactor. Tests MUST be written before any implementation code. All failing tests MUST be approved before implementation begins. Code coverage MUST remain above 90% for domain logic. Integration tests MUST verify event store contracts and projections.

**Rationale**: TDD ensures design quality, prevents regressions, and creates living documentation. High test coverage maintains confidence during refactoring and architectural changes.

### IV. Architectural Separation
CLI applications, HTTP applications, and domain implementations MUST remain strictly separated. Domain layer has no dependencies on application layers. Application layers orchestrate domain operations but contain no business logic. Infrastructure adapters MUST implement domain interfaces defined in the domain layer.

**Rationale**: Separation enables independent testing, deployment flexibility, and prevents technology concerns from polluting business logic.

### V. In-Memory Implementation Priority
All EventStore abstractions MUST start with in-memory implementations. Persistence adapters are secondary concerns added only when required. In-memory implementations MUST support full event store semantics including ordering, filtering, and projections. Performance testing MUST validate in-memory bounds before considering persistent storage.

**Rationale**: In-memory implementations reduce complexity, enable faster testing, and clarify core abstractions before introducing persistence concerns.

## Architecture Constraints

**Technology Stack**: Gradle Kotlin project using Kotlin multiplatform capabilities where beneficial. No external frameworks for domain logic - pure Kotlin constructs only. Infrastructure libraries (HTTP servers, serialization) confined to application and infrastructure layers.

**Module Structure**: Separate Gradle modules for `domain`, `cli-app`, `http-app`, and `infrastructure`. Domain module MUST NOT depend on other modules. Application modules MAY depend on domain and infrastructure. Clear dependency direction enforced via Gradle constraints.

**Event Store Interface**: Single EventStore abstraction supporting append, read, and subscribe operations. Events are immutable data classes with versioning metadata. Projections built as separate read models updated via event handlers.

## Development Workflow

**Code Reviews**: All commits MUST pass automated tests and architectural compliance checks. Domain purity verified through dependency analysis. Event schemas reviewed for backward compatibility. TDD cycle evidence required (test commits before implementation commits).

**Quality Gates**: Compilation must succeed with zero warnings. All tests must pass including integration tests. Code coverage reports generated for each pull request. Performance benchmarks run for event store operations.

**Documentation**: Domain concepts documented in ubiquitous language glossary. Event schemas maintained in versioned documentation. CLI and HTTP API contracts specified before implementation.

## Governance

This constitution supersedes all other development practices and architectural decisions. Amendments require written justification, impact analysis, and explicit approval. All pull requests MUST demonstrate compliance with these principles. Complexity that violates these constraints MUST be simplified rather than accommodated through exceptions.

Use `Agents.md` for runtime TDD guidance and implementation patterns that support these constitutional requirements.

**Version**: 1.0.0 | **Ratified**: 2025-09-30 | **Last Amended**: 2025-09-30
