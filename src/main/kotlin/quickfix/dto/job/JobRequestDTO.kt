package quickfix.dto.job

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.utils.exceptions.BusinessException

@Schema(description = "Solicitud de un Job por un customer, consumido por observers")
data class JobRequestDTO(
    var customerId: Long,
    // TODO: cambiar professional a string por ahora lo dejo asi
    var professionId: Long,
    var detail: String,
    var addressId: Long
)

fun JobRequestDTO.validate() {

    validDetail(detail)
}

private fun validProfession(profession: String) { if(profession.isBlank()) throw BusinessException() }
private fun validDetail(detail: String) { if(detail.isBlank()) throw BusinessException() }
