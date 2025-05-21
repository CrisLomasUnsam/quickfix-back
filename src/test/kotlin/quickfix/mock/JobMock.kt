package quickfix.mock

import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import java.time.LocalDateTime

data class JobMock(
    val job: Job,
    val customer: User,
    val professional: User
)

fun createJobMock (
    customer: User,
    professional: User,
    profession: Profession,
    price: Double

) : JobMock {

    val job = Job().apply {
        this.customer = customer
        this.professional = professional
        this.price = price
        this.initDateTime = LocalDateTime.now()
        this.status = JobStatus.DONE
        this.profession = profession
        this.description = ""
    }


    return JobMock(job, customer, professional)
}
