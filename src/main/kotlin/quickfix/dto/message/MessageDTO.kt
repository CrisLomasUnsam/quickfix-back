package quickfix.dto.message

data class MessageDTO(
    val userFromId: Long,
    val userToId: Long,
    val jobId: Long,
    val msg: String
)

fun MessageDTO.toRedisMessage() = RedisMessageDTO(
    msg = this.msg,
    timestamp = System.currentTimeMillis()
)