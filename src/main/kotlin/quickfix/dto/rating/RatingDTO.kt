package quickfix.dto.rating

import quickfix.models.Rating

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