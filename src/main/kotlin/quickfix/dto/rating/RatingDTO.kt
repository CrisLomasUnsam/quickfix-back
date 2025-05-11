package quickfix.dto.rating

import quickfix.models.Rating
import quickfix.utils.YearAndMonthformatter
import quickfix.utils.stringifyDate

data class RatingDTO(
    val jobId: Long,
    val score: Int,
    val comment: String
)

fun Rating.toDTO(): RatingDTO =
    RatingDTO(
        jobId = this.job.id,
        score = this.score,
        comment = this.comment
    )