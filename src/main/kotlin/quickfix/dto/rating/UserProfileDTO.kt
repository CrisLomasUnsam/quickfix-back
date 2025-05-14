package quickfix.dto.rating

import quickfix.models.RatingStatsProjection
import quickfix.models.User

data class UserProfileDTO(
    var id: Long,
    var name: String,
    var lastName: String,
    var verified: Boolean,
    var totalJobsFinished: Int,
    var otherProfessions: List<String>,
    val ratings: RatingsStatsDTO
) {
    companion object {
        fun from(
            user: User,
            totalJobsFinished: Int,
            otherProfessions: List<String>,
            stats: RatingStatsProjection
        ): UserProfileDTO= UserProfileDTO (
                id = user.id,
                name = user.name,
                lastName = user.lastName,
                verified = user.verified,
                totalJobsFinished = totalJobsFinished,
                otherProfessions = otherProfessions,
                ratings           = RatingsStatsDTO.from(stats)
        )

    }
}