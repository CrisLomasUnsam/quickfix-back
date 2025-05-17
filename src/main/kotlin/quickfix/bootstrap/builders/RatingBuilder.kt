package quickfix.bootstrap.builders


import quickfix.models.Job
import quickfix.models.Rating
import quickfix.models.User
import java.time.LocalDate

class RatingBuilder {
    companion object{
        fun buildMock(userFrom: User, job: Job, score: Int): Rating {

            val userTo = if (job.customer == userFrom) job.professional else job.customer

            return Rating().apply {
                this.userFrom = userFrom
                this.userTo = userTo
                this.job = job
                this.score = score
                this.comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi magna odio, finibus eu mi ut, elementum fringilla mauris."
                this.yearAndMonth = LocalDate.now()
            }
        }
    }
}