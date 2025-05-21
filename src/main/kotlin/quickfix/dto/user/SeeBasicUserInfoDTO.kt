package quickfix.dto.user

import quickfix.models.User
import quickfix.utils.functions.getAvatarUrl

data class SeeBasicUserInfoDTO  (
    var id: Long,
    var name: String,
    var lastName: String,
    var avatar: String,
    var verified: Boolean,
    var averageRating: Double,
    var totalRatings: Int? = null,
    var hasVehicle: Boolean? = null
) {
    companion object {
        fun toDto(user: User, seeCustomerInfo: Boolean, totalRatings: Int? = null): SeeBasicUserInfoDTO {

            val userAverageRating =
                if(seeCustomerInfo) user.averageRating else user.professionalInfo.averageRating

            return SeeBasicUserInfoDTO(
                id = user.id,
                name = user.name,
                lastName = user.lastName,
                avatar = getAvatarUrl(user.id),
                verified = user.verified,
                averageRating = userAverageRating,
                totalRatings = totalRatings,
                hasVehicle = if(seeCustomerInfo) null else user.professionalInfo.hasVehicle
            )
        }
    }
}