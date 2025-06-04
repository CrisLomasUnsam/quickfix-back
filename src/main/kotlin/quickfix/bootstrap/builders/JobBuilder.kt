package quickfix.bootstrap.builders

import quickfix.utils.functions.stringifyDateTime

import quickfix.dto.job.jobOffer.CreateJobOfferDTO
import quickfix.dto.job.jobRequest.CreateJobRequestDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import quickfix.utils.jobs.getJobRequestKey
import java.time.LocalDateTime

class JobBuilder {
    companion object{
        fun buildMock(customer: User, professional: User, profession: Profession, done : Boolean = true) =
            Job().apply {
                this.customer = customer
                this.professional = professional
                this.initDateTime = if(done) LocalDateTime.now().minusDays(1) else LocalDateTime.now().plusDays(1)
                this.profession = profession
                this.status = if(done) JobStatus.DONE else JobStatus.PENDING
                this.price = 19999.0
                this.detail = "Lorem ipsum dolor sit amet consectetur adipiscing elit aliquam, morbi ad ornare pharetra posuere ut fringilla molestie tristique."
                this.duration = 10
                this.durationUnit = "Minutos"
            }
    }
}

class JobRequestBuilder {
    companion object{
        fun buildMock(profession: Profession, dayOffset: Long, isInstantRequest: Boolean = false) : CreateJobRequestDTO {
            val dateTime = if (isInstantRequest) LocalDateTime.now() else LocalDateTime.now().plusDays(1 + dayOffset)
            return CreateJobRequestDTO(
                professionId = profession.id,
                detail = "Lorem ipsum dolor em sit amet lo gump samar ipsum it.",
                streetAddress = "Calle siempre viva 123",
                streetReference = "Toque timbre",
                //Le pongo un dayOffset para que no falle el dataInitializer por detectar que el profesional ya tiene una oferta activa ese día
                neededDatetime = stringifyDateTime(dateTime),
                instantRequest = isInstantRequest
            )
        }

    }
}

class JobOfferBuilder {
    companion object{
        fun buildMock(customer: User, profession: Profession) =
            CreateJobOfferDTO(
                requestId = getJobRequestKey(profession.id, customer.id),
                duration = 45,
                durationUnit = "Minutos",
                price = 10.999
            )
    }
}