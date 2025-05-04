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

fun createRatingMock(userFrom: User, userTo: User, job: Job, score: Int, date: LocalDate): RatingMock {

    val rating = Rating().apply {
        this.userFrom = userFrom
        this.userTo = userTo
        this.job = job
        this.score = score
        this.comment = "Esto es una rating"
        this.yearAndMonth = date
    }

    return RatingMock(rating,job,userFrom,userTo)
}
