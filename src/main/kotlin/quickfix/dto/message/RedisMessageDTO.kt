package quickfix.dto.message

data class RedisMessageDTO(
    val msg : String,
    val senderIsCustomer: Boolean,
    val timestamp : Long
)
