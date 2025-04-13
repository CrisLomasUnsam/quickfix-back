package quickfix.dto.message

data class ChatMessageDTO(
    val customerId: Long,
    val professionalId: Long,
    val jobId: Long,
    val msg: String
)

fun ChatMessageDTO.toRedisMessage() = RedisMessageDTO(
    msg = this.msg,
    timestamp = System.currentTimeMillis()
)