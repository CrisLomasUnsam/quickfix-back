package quickfix.dto.message

data class MessageDTO(
    val jobId: Long,
    val message: String,
)

fun MessageDTO.toRedisMessage(senderIsCustomer : Boolean) = RedisMessageDTO(
    message = this.message,
    senderIsCustomer = senderIsCustomer,
    timestamp = System.currentTimeMillis()
)