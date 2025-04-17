package quickfix.dto.message

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class RedisMessageDTO @JsonCreator constructor(

    @JsonProperty("msg")
    val msg: String,

    @JsonProperty("senderIsCustomer")
    val senderIsCustomer: Boolean,

    @JsonProperty("timestamp")
    val timestamp: Long
)
