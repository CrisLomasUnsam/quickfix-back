package quickfix.dto.rating

import quickfix.models.Rating
import quickfix.utils.stringifyDate

data class RatingDTO(
    val userFromId: Long,
    val userToId: Long,
    val jobId: Long,
    val score: Int,
    val yearAndMonth: String,
    val comment: String
)

fun Rating.toDTO(): RatingDTO =
    RatingDTO(
        userFromId = this.userFrom.id,
        userToId = this.userTo.id,
        jobId = this.job.id,
        score = this.score,
        yearAndMonth = stringifyDate(this.yearAndMonth),
        comment = this.comment
    )