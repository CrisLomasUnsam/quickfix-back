package quickfix.dto.rating

import quickfix.models.Rating
import quickfix.models.User
import quickfix.utils.functions.getAvatarUrl


data class RateUserPageDTO(
    val name: String,
    val lastName: String,
    val verified: Boolean,
    val averageRating: Double,
    val professionName: String,
    var avatar: String,
    val score: Int?,
    val comment: String?

){
    companion object {
        fun from(userTo: User, professionName: String, existingRating: Rating?): RateUserPageDTO {
            return RateUserPageDTO(
                name = userTo.name,
                lastName = userTo.lastName,
                verified = userTo.verified,
                averageRating = userTo.averageRating,
                professionName = professionName,
                avatar = getAvatarUrl(userTo.id),
                score = existingRating?.score,
                comment = existingRating?.comment
            )
        }
    }
}

