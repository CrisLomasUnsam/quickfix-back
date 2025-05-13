package quickfix.dto.rating

import quickfix.models.Job
import quickfix.models.Rating
import quickfix.models.User
import java.util.*


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
        fun from(userTo: User, averageRating: Double, existingRating: Rating?, job: Job): RateUserPageDTO {
            return RateUserPageDTO(
                name = userTo.name,
                lastName = userTo.lastName,
                verified = userTo.verified,
                averageRating = averageRating,
                professionName = job.profession.name,
                avatar = "/user/avatar/${userTo.id}",
                score = existingRating?.score,
                comment = existingRating?.comment
            )
        }
    }
}

