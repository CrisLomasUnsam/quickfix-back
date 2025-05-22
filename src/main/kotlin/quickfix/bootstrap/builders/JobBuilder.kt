package quickfix.bootstrap.builders

import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.dto.job.jobRequest.CreateJobRequestDTO
import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.dto.user.SeeBasicUserInfoDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.enums.JobStatus
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
                this.description = "Lorem ipsum dolor sit amet consectetur adipiscing elit aliquam, morbi ad ornare pharetra posuere ut fringilla molestie tristique."
                this.duration = 10
                this.durationUnit = "Minutos"
            }
    }
}

class JobRequestBuilder {
    companion object{
        fun buildMock(customer: User, profession: Profession, isInstantRequest: Boolean = false): CreateJobRequestDTO {
            return CreateJobRequestDTO(
                userId = customer.id,
                serviceId = profession.id,
                detail = "Lorem ipsum dolor em sit amet lo gump samar ipsum it.",
                neededDatetime = if (isInstantRequest) LocalDateTime.now() else LocalDateTime.now().plusDays(1),
                instantRequest = isInstantRequest
            )
        }
    }
}

class JobOfferBuilder {
    companion object{
        fun buildMock(customer: User, professional: User, profession: Profession, dayOffset: Long) =
            JobOfferDTO(
                customer = SeeBasicUserInfoDTO.toDto(customer, seeCustomerInfo = true),
                profession = profession,
                professional = SeeBasicUserInfoDTO.toDto(professional, seeCustomerInfo = false),
                price = 10900.99,
                distance = 10.2,
                estimatedArriveTime = 35,
                jobDuration = 50,
                jobDurationTimeUnit = "Minutos",
                neededDatetime = LocalDateTime.now().plusDays(10 + dayOffset),
                detail = "Mock de job offer. Lorem ipsum dolorem et ni hus tar",
                instantRequest = false
            )
    }
}