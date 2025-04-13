package quickfix.dto.message

data class ChatMessageDTO(
    val customerId: Long,
    val professionalId: Long,
    val jobId: Long,
    val msg: String,
    val senderIsCustomer : Boolean
)

fun ChatMessageDTO.toRedisMessage() = RedisMessageDTO(
    msg = this.msg,
    senderIsCustomer = this.senderIsCustomer,
    timestamp = System.currentTimeMillis()
)