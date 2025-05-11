package quickfix.dto.message

data class MessageDTO(
    val jobId: Long,
    val msg: String,
)

fun MessageDTO.toRedisMessage(senderIsCustomer : Boolean) = RedisMessageDTO(
    msg = this.msg,
    senderIsCustomer = senderIsCustomer,
    timestamp = System.currentTimeMillis()
)