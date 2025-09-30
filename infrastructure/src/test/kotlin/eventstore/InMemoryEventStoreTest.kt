package eventstore

import cart.CartId
import cart.ProductId
import events.CartCreated
import events.DomainEvent
import events.EventVersion
import events.ItemAddedToCart
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InMemoryEventStoreTest {

    @Test
    fun `should append events to empty stream`() {
        val eventStore = InMemoryEventStore()
        val cartId = CartId.generate()
        val events = listOf(
            CartCreated(cartId),
            ItemAddedToCart(cartId, ProductId("prod-1"), 1, 1)
        )
        
        val result = eventStore.append(cartId.value, EventVersion.initial, events)
        
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should retrieve events for aggregate`() {
        val eventStore = InMemoryEventStore()
        val cartId = CartId.generate()
        val events = listOf(
            CartCreated(cartId),
            ItemAddedToCart(cartId, ProductId("prod-1"), 1, 1)
        )
        
        eventStore.append(cartId.value, EventVersion.initial, events)
        val retrievedEvents = eventStore.getEvents(cartId.value)
        
        assertEquals(2, retrievedEvents.size)
        assertTrue(retrievedEvents[0] is CartCreated)
        assertTrue(retrievedEvents[1] is ItemAddedToCart)
    }

    @Test
    fun `should fail when expected version is wrong`() {
        val eventStore = InMemoryEventStore()
        val cartId = CartId.generate()
        
        // First append
        eventStore.append(cartId.value, EventVersion.initial, listOf(CartCreated(cartId)))
        
        // Second append with wrong expected version
        val result = eventStore.append(
            cartId.value, 
            EventVersion.initial, // Should be version 1, not 0
            listOf(ItemAddedToCart(cartId, ProductId("prod-1"), 1, 1))
        )
        
        assertTrue(result.isFailure)
    }

    @Test
    fun `should return events from specific version`() {
        val eventStore = InMemoryEventStore()
        val cartId = CartId.generate()
        
        // Append multiple events
        eventStore.append(cartId.value, EventVersion.initial, listOf(CartCreated(cartId)))
        eventStore.append(cartId.value, EventVersion(1), listOf(ItemAddedToCart(cartId, ProductId("prod-1"), 1, 1)))
        eventStore.append(cartId.value, EventVersion(2), listOf(ItemAddedToCart(cartId, ProductId("prod-2"), 1, 2)))
        
        // Get events from version 1
        val eventsFromVersion1 = eventStore.getEvents(cartId.value, EventVersion(1))
        
        assertEquals(2, eventsFromVersion1.size) // Should get events at version 1 and 2
    }

    @Test
    fun `should return empty list for non-existent aggregate`() {
        val eventStore = InMemoryEventStore()
        val events = eventStore.getEvents("non-existent")
        
        assertTrue(events.isEmpty())
    }
}
