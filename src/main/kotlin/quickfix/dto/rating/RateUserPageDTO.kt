package quickfix.dto.rating

import quickfix.models.Rating
import quickfix.models.User
import quickfix.utils.functions.getAvatarUrl


data class RateUserPageDTO(
    val userId: Long,
    val name: String,
    val lastName: String,
    val verified: Boolean,
    val professionId: Long,
    var avatar: String,
    val score: Int?,
    val comment: String?

){
    companion object {
        fun from(userTo: User, professionId: Long, existingRating: Rating?): RateUserPageDTO {
            return RateUserPageDTO(
                userId = userTo.id,
                name = userTo.name,
                lastName = userTo.lastName,
                verified = userTo.verified,
                professionId = professionId,
                avatar = getAvatarUrl(userTo.id),
                score = existingRating?.score,
                comment = existingRating?.comment
            )
        }
    }
}

