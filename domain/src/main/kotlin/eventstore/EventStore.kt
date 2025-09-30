package eventstore

import events.DomainEvent
import events.EventVersion

interface EventStore {
    fun append(
        aggregateId: String,
        expectedVersion: EventVersion,
        events: List<DomainEvent>
    ): Result<Unit>
    
    fun getEvents(aggregateId: String): List<DomainEvent>
    
    fun getEvents(aggregateId: String, fromVersion: EventVersion): List<DomainEvent>
}
