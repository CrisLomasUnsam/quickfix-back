package quickfix.bootstrap.builders

import quickfix.dto.job.jobRequest.ProfessionalJobRequestDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.getAvatarUrl
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
                this.duration = 10
                this.durationUnit = "Minutos"
            }
    }
}

class JobRequestBuilder {
    companion object{
        fun buildMock(customer: User, profession: Profession, isInstantRequest: Boolean = false) =
            ProfessionalJobRequestDTO(
                customerId = customer.id,
                name = customer.name,
                lastName = customer.lastName,
                avatar = getAvatarUrl(customer.id),
                professionId = profession.id,
                professionName = profession.name,
                detail = "Lorem ipsum dolor em sit amet lo gump samar ipsum it.",
                rating = (Math.random() % 5) + 1.0,
                neededDatetime = if(isInstantRequest) LocalDateTime.now() else LocalDateTime.now().plusDays(1),
                instantRequest = isInstantRequest
            )
    }
}