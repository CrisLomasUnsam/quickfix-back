package quickfix.dto.user

import quickfix.dto.rating.RatingDTO
import quickfix.dto.rating.toDTO
import quickfix.models.Rating
import quickfix.models.User
import quickfix.utils.functions.getAvatarUrl

data class SeeUserRatingsDTO (
    val name: String,
    val lastName: String,
    val verified: Boolean,
    val avatar: String,
    val averageRating: Double,
    val ratings: List<RatingDTO>
) {
    companion object {
        fun toDTO(user: User, ratings: List<Rating>): SeeUserRatingsDTO =
            SeeUserRatingsDTO(
                name = user.name,
                lastName = user.lastName,
                verified = user.verified,
                avatar = getAvatarUrl(user.id),
                averageRating = user.averageRating,
                ratings = ratings.map{ it.toDTO() }
            )
    }
}