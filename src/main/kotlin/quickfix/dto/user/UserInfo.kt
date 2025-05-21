package quickfix.dto.user

import quickfix.models.User
import quickfix.utils.functions.getAvatarUrl
import kotlin.math.roundToInt

data class UserInfo(
    val id: Long,
    val name: String,
    val lastName: String,
    val avatar: String,
    val verified: Boolean,
    val averageRating: Double,
    val totalEarnings: Double?
) {
    companion object {
        fun toDTO(
            user: User,
            requesterIsCustomer: Boolean

        ): UserInfo{
            return UserInfo (
                id = user.id,
                name = user.name,
                lastName = user.lastName,
                avatar = getAvatarUrl(user.id),
                verified = user.verified,
                averageRating = if (!requesterIsCustomer) { user.professionalInfo.averageRating } else { user.averageRating }.let { (it * 100).roundToInt() / 100.0 },
                totalEarnings = user.professionalInfo.balance
            )
        }
    }
}

