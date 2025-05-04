package quickfix.mock

import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import java.time.LocalDate

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
        this.date = LocalDate.now().minusDays(1)
        this.initDateTime = LocalDate.now()
        this.status = JobStatus.DONE
        this.done = true
        this.profession = profession
    }


    return JobMock(job, customer, professional)
}
