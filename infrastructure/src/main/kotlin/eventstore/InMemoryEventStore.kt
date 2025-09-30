package eventstore

import events.DomainEvent
import events.EventVersion
import java.time.Instant

data class EventEnvelope(
    val aggregateId: String,
    val aggregateType: String,
    val eventType: String,
    val event: DomainEvent,
    val version: EventVersion,
    val timestamp: Instant
)

class InMemoryEventStore : EventStore {
    private val eventStreams = mutableMapOf<String, MutableList<EventEnvelope>>()
    private val versions = mutableMapOf<String, EventVersion>()

    override fun append(
        aggregateId: String,
        expectedVersion: EventVersion,
        events: List<DomainEvent>
    ): Result<Unit> {
        return try {
            val currentVersion = versions[aggregateId] ?: EventVersion.initial
            
            if (currentVersion != expectedVersion) {
                return Result.failure(OptimisticLockingException(
                    "Expected version $expectedVersion but current version is $currentVersion"
                ))
            }

            val eventList = eventStreams.getOrPut(aggregateId) { mutableListOf() }
            var version = currentVersion
            
            events.forEach { event ->
                version = version.increment()
                val envelope = EventEnvelope(
                    aggregateId = aggregateId,
                    aggregateType = "Cart", // TODO: Make this generic
                    eventType = event::class.simpleName!!,
                    event = event,
                    version = version,
                    timestamp = Instant.now()
                )
                eventList.add(envelope)
            }
            
            versions[aggregateId] = version
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getEvents(aggregateId: String): List<DomainEvent> {
        return eventStreams[aggregateId]?.map { it.event } ?: emptyList()
    }

    override fun getEvents(aggregateId: String, fromVersion: EventVersion): List<DomainEvent> {
        return eventStreams[aggregateId]
            ?.filter { it.version.value > fromVersion.value }
            ?.map { it.event } ?: emptyList()
    }
}

class OptimisticLockingException(message: String) : Exception(message)
