package quickfix.dto.chat

import quickfix.utils.functions.dateTimeFromTimestamp
import quickfix.utils.functions.stringifyDateTimeWithoutYear

data class RedisMessageDTO (
    val message: String,
    val senderIsCustomer: Boolean,
    val timestamp: Long
)


fun RedisMessageDTO.toMessageResponseDTO(requesterIsCustomer : Boolean) = MessageResponseDTO(
    message = this.message,
    itsMine = (senderIsCustomer && requesterIsCustomer) || (!senderIsCustomer && !requesterIsCustomer),
    datetime = stringifyDateTimeWithoutYear(dateTimeFromTimestamp(this.timestamp))
)