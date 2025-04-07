package quickfix.models
object GlobalIdGenerator {
    private var currentId: Long = 0
    fun nextId(): Long = currentId++
}