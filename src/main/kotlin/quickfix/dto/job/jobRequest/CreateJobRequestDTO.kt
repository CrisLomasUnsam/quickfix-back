package quickfix.dto.job.jobRequest

import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.utils.MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST
import quickfix.utils.exceptions.DetailException
import quickfix.utils.exceptions.IllegalDataException
import quickfix.utils.exceptions.ProfessionException
import quickfix.utils.functions.DatetimeFormatter
import quickfix.utils.functions.parseDatetime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class CreateJobRequestDTO(
    val userId: Long,
    var serviceId: Long,
    var detail: String,
    val neededDatetime: String,
    var instantRequest: Boolean = false
){



    fun validate() {
        if (serviceId < 1) throw ProfessionException("ID de profesión inválido")
        if (detail.isBlank()) throw DetailException("El detalle no puede estar vacío")

        val parsedDatetime = parseDatetime(neededDatetime)
        if (parsedDatetime.isBefore(LocalDateTime.now().minusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST))) {
            throw IllegalDataException("La fecha y hora no pueden ser anteriores a la fecha actual")
        }
    }

    fun toJobRequestDTO(customer: SeeBasicUserInfoDTO): JobRequestDTO =
        JobRequestDTO(
            customer = customer,
            professionId = this.serviceId,
            detail = this.detail,
            neededDatetime = parseDatetime(neededDatetime)
        )
}