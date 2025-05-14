package quickfix.dto.rating

import quickfix.models.RatingStatsProjection
import quickfix.models.User

data class UserProfileDTO(
    var id: Long,
    var name: String,
    var lastName: String,
    var verified: Boolean,
    var totalJobsFinished: Int,
    val ratings: RatingsStatsDTO
) {
    companion object {
        fun from(
            user: User,
            totalJobsFinished: Int,
            stats: RatingStatsProjection
        ): UserProfileDTO= UserProfileDTO (
                id = user.id,
                name = user.name,
                lastName = user.lastName,
                verified = user.verified,
                totalJobsFinished = totalJobsFinished,
                ratings           = RatingsStatsDTO.from(stats)
        )

    }
}