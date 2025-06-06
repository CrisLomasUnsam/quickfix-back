package quickfix.dto.job.jobRequest

import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.utils.INSTANT_REQUEST_LIVE_DAYS
import quickfix.utils.MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST
import quickfix.utils.exceptions.DetailException
import quickfix.utils.exceptions.IllegalDataException
import quickfix.utils.exceptions.ProfessionException
import quickfix.utils.functions.parseDatetime
import quickfix.utils.functions.stringifyDateTime
import java.time.LocalDateTime

data class CreateJobRequestDTO(
    var professionId: Long,
    var detail: String,
    var streetAddress: String,
    var streetReference: String,
    var neededDatetime: String,
    var instantRequest: Boolean?
){

    fun validate() {

        if(instantRequest == null)
            instantRequest = false

        if (professionId < 1) throw ProfessionException("ID de profesión inválido")
        if (streetAddress.isBlank()) throw DetailException("La dirección no puede estar vacía")
        if (detail.isBlank()) throw DetailException("El detalle no puede estar vacío")

        if(instantRequest == true){
            neededDatetime = stringifyDateTime(LocalDateTime.now())
            return
        }

        val parsedDatetime = parseDatetime(neededDatetime)

        //Si la fecha y hora son anteriores a 5 minutos en el PASADO, lanzamos error
        if (parsedDatetime.isBefore(LocalDateTime.now().minusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST)))
            throw IllegalDataException("La fecha y hora no pueden ser anteriores a la fecha actual")

        //Si la fecha y hora son posteriores a los días que definimos como tiempo de vida de las requests, lanzamos error
        if (parsedDatetime.isAfter(LocalDateTime.now().plusDays(INSTANT_REQUEST_LIVE_DAYS)))
            throw IllegalDataException("Debe elegir una fecha que esté dentro de los próximos $INSTANT_REQUEST_LIVE_DAYS días.")

        //Si la fecha y hora son anteriores a 5 minutos en el FUTURO, consideramos que es una instantRequest
        if (parsedDatetime.isBefore(LocalDateTime.now().plusMinutes(MINUTES_TO_BE_CONSIDERED_FUTURE_REQUEST)))
            this.instantRequest = true
    }

    fun toJobRequestDTO(customer: SeeBasicUserInfoDTO): JobRequestDTO =
        JobRequestDTO(
            customer = customer,
            professionId = this.professionId,
            detail = this.detail.trim(),
            streetAddress = this.streetAddress.trim(),
            streetReference = this.streetReference.trim(),
            neededDatetime = parseDatetime(neededDatetime),
            instantRequest = this.instantRequest!!
        )
}