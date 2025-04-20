package quickfix.dto.job

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.utils.exceptions.BusinessException

@Schema(description = "Solicitud de un Job por un customer, consumido por observers")
data class JobRequestDTO(
    var customerId: Long,
    var profession: String,
    var detail: String,
    var professionId: Long? = null
)

fun JobRequestDTO.validate() {
    validProfession(profession)
    validDetail(detail)
}

private fun validProfession(profession: String) { if(profession.isBlank()) throw BusinessException() }
private fun validDetail(detail: String) { if(detail.isBlank()) throw BusinessException() }
