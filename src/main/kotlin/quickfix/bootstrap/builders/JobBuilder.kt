package quickfix.bootstrap.builders

import quickfix.dto.job.jobRequest.JobRequestDTO
import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.getAvatarUrl
import java.time.LocalDate
import java.time.LocalDateTime

class JobBuilder {
    companion object{
        fun buildMock(customer: User, professional: User, profession: Profession, done : Boolean) =
            Job().apply {
                this.customer = customer
                this.professional = professional
                this.date = if(done) LocalDate.now().minusDays(1) else LocalDate.now().plusDays(1)
                this.done = done
                this.profession = profession
                this.status = if(done) JobStatus.DONE else JobStatus.PENDING
                this.initDateTime = LocalDate.now().minusDays(2)
                this.price = 19999.0
            }
    }
}

class JobRequestBuilder {
    companion object{
        fun buildMock(customer: User, profession: Profession, isFutureRequest: Boolean = false) =
            JobRequestDTO(
                customerId = customer.id,
                name = customer.name,
                lastName = customer.lastName,
                avatar = getAvatarUrl(customer.id),
                professionId = profession.id,
                professionName = profession.name,
                detail = "Lorem ipsum dolor em sit amet lo gump samar ipsum it.",
                rating = (Math.random() % 5) + 1.0,
                neededDatetime = if(isFutureRequest) LocalDateTime.now().plusDays(7) else LocalDateTime.now()
            )
    }
}