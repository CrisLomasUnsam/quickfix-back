package quickfix.dto.job.jobRequest

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import quickfix.utils.exceptions.BusinessException

@Schema(description = "Solicitud de un Job por un customer, consumido por observers")
data class JobRequestDTO @JsonCreator constructor(

    @JsonProperty("customerId")
    var customerId: Long,

    @JsonProperty("professionId")
    var professionId: Long,

    @JsonProperty("detail")
    var detail: String
)



fun JobRequestDTO.validate() {
    validProfession(professionId)
    validDetail(detail)
}

private fun validProfession(professionId: Long) { if(professionId < 1) throw BusinessException() }
private fun validDetail(detail: String) { if(detail.isBlank()) throw BusinessException() }
