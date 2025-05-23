package quickfix.dto.job.jobRequest

import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.utils.MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST
import quickfix.utils.exceptions.DetailException
import quickfix.utils.exceptions.IllegalDataException
import quickfix.utils.exceptions.ProfessionException
import quickfix.utils.functions.parseDatetime
import java.time.LocalDateTime

data class CreateJobRequestDTO(
    var professionId: Long,
    var detail: String,
    val neededDatetime: String,
    var instantRequest: Boolean?
){

    fun validate() {

        if(instantRequest == null)
            instantRequest = false

        if (professionId < 1) throw ProfessionException("ID de profesión inválido")
        if (detail.isBlank()) throw DetailException("El detalle no puede estar vacío")

        val parsedDatetime = parseDatetime(neededDatetime)

        //Si la fecha y hora son anteriores a 5 minutos en el PASADO, lanzamos error
        if (parsedDatetime.isBefore(LocalDateTime.now().minusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST)))
            throw IllegalDataException("La fecha y hora no pueden ser anteriores a la fecha actual")

        //Si la fecha y hora son anteriores a 5 minutos en el FUTURO, consideramos que es una instantRequest
        if (parsedDatetime.isBefore(LocalDateTime.now().plusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST)))
            this.instantRequest = true
    }

    fun toJobRequestDTO(customer: SeeBasicUserInfoDTO): JobRequestDTO =
        JobRequestDTO(
            customer = customer,
            professionId = this.professionId,
            detail = this.detail,
            neededDatetime = parseDatetime(neededDatetime),
            instantRequest = this.instantRequest!!
        )
}