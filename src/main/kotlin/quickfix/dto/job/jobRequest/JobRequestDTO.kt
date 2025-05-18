package quickfix.dto.job.jobRequest

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import quickfix.utils.MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST
import quickfix.utils.exceptions.CustomerException
import quickfix.utils.exceptions.DetailException
import quickfix.utils.exceptions.IllegalDataException
import quickfix.utils.exceptions.ProfessionException
import java.time.LocalDateTime

@Schema(description = "Solicitud de un Job por un customer")
data class JobRequestDTO @JsonCreator constructor(

    @JsonProperty("customerId")
    var customerId: Long,

    @JsonProperty("name")
    var name: String,

    @JsonProperty("lastName")
    var lastName: String,

    @JsonProperty("avatar")
    var avatar: String,

    @JsonProperty("professionId")
    var professionId: Long,

    @JsonProperty("professionName")
    var professionName: String,

    @JsonProperty("neededDatetime")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    var neededDatetime: LocalDateTime?,

    @JsonProperty("detail")
    var detail: String,

    @JsonProperty("rating")
    var rating: Double

)

fun JobRequestDTO.validate() {
    validCustomer(customerId)
    validProfession(professionId)
    validDetail(detail)
    validDatetime(neededDatetime)
    if(neededDatetime == null || neededDatetime!!.isBefore(LocalDateTime.now()))
        neededDatetime = LocalDateTime.now()
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

private fun validDatetime(dateTime: LocalDateTime?) {
    if(dateTime != null && dateTime.isBefore(LocalDateTime.now().minusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST))) {
        throw IllegalDataException("La fecha y hora de solicitud de servicio no pueden ser anteriores a la fecha y hora actuales")
    }
}

