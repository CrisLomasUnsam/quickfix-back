package quickfix.mock

import quickfix.models.Job
import quickfix.models.Rating
import quickfix.models.User
import java.time.LocalDate

data class RatingMock (
    val rating: Rating,
    val job: Job,
    val fromUser: User,
    val toUser: User
)

fun createRatingMock(userFrom: User, userTo: User, job: Job): RatingMock {

    val rating = Rating().apply {
        this.userFrom = userFrom
        this.userTo = userTo
        this.job = job
        this.score = 5
        this.comment = "Excelente"
        this.yearAndMonth = LocalDate.now()
    }

    return RatingMock(rating,job,userFrom,userTo)
}
