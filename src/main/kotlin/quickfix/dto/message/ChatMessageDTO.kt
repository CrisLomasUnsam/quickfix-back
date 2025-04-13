package quickfix.dto.message

data class ChatMessageDTO(
    val userFromId: Long,
    val userToId: Long,
    val jobId: Long,
    val msg: String
)

fun ChatMessageDTO.toRedisMessage() = RedisMessageDTO(
    msg = this.msg,
    timestamp = System.currentTimeMillis()
)