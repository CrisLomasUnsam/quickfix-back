package quickfix.bootstrap.builders

import quickfix.models.Job
import quickfix.models.Profession
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import java.time.LocalDate

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