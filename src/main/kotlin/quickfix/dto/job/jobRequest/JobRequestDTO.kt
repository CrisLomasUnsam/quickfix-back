package quickfix.dto.job.jobRequest

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.utils.MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST
import quickfix.utils.exceptions.CustomerException
import quickfix.utils.exceptions.DetailException
import quickfix.utils.exceptions.IllegalDataException
import quickfix.utils.exceptions.ProfessionException
import java.time.LocalDateTime

@Schema(description = "JobRequest original, es la que se almacena en Redis")
data class JobRequestDTO (
    var customer: SeeBasicUserInfoDTO,
    var professionId: Long,
    var detail: String,
    var neededDatetime: LocalDateTime,
    var instantRequest: Boolean = false
)

fun JobRequestDTO.validate() {
    validCustomer(customer.id)
    validProfession(professionId)
    validDetail(detail)
    validDatetime(neededDatetime)
    setInstantRequest()
}

private fun JobRequestDTO.setInstantRequest(){
    if(neededDatetime.isBefore(LocalDateTime.now().plusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST)))
        instantRequest = true
}

private fun validCustomer(customerId: Long) {
    if(customerId < 1) throw CustomerException("No existe un customer con ese ID")
}

private fun validProfession(professionId: Long) {
    if(professionId < 1) throw ProfessionException("El ID de la profesión no matchea con una profesión en la BBDD")
}

private fun validDetail(detail: String) {
    if(detail.isBlank()) throw DetailException("EL detalle no puede estar vacío")
}

private fun validDatetime(dateTime: LocalDateTime) {
    if(dateTime.isBefore(LocalDateTime.now().minusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST))) {
        throw IllegalDataException("La fecha y hora de solicitud de servicio no pueden ser anteriores a la fecha y hora actuales")
    }
}

