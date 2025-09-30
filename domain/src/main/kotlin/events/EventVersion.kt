package events

@JvmInline
value class EventVersion(val value: Long) {
    init {
        require(value >= 0) { "Version cannot be negative" }
    }
    
    fun increment(): EventVersion = EventVersion(value + 1)
    
    companion object {
        val initial = EventVersion(0)
    }
}
