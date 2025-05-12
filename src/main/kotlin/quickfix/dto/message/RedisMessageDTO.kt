package quickfix.dto.message

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import quickfix.utils.CustomDateTimeFormatter
import quickfix.utils.dateTimeFromTimestamp
import quickfix.utils.stringifyDateTime

data class RedisMessageDTO @JsonCreator constructor(

    @JsonProperty("message")
    val message: String,

    @JsonProperty("senderIsCustomer")
    val senderIsCustomer: Boolean,

    @JsonProperty("timestamp")
    val timestamp: Long
)


fun RedisMessageDTO.toMessageResponseDTO(requesterIsCustomer : Boolean) = MessageResponseDTO(
    message = this.message,
    itsMine = (senderIsCustomer && requesterIsCustomer) || (!senderIsCustomer && !requesterIsCustomer),
    datetime = stringifyDateTime(dateTimeFromTimestamp(this.timestamp), CustomDateTimeFormatter)
)