package quickfix.dto.rating

import quickfix.models.Rating

data class RatingDTO(
    val userFromId: Long,
    val userToId: Long,
    val jobId: Long,
    val score: Int,
    val comment: String
)

fun Rating.toDTO(): RatingDTO =
    RatingDTO(
        userFromId = this.userFrom.id,
        userToId = this.userTo.id,
        jobId = this.job.id,
        score = this.score,
        comment = this.comment
    )