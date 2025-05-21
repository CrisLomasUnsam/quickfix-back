package quickfix.dto.user

import quickfix.models.User
import quickfix.utils.functions.getAvatarUrl

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
                averageRating = if (!requesterIsCustomer) { user.professionalInfo.averageRating } else { user.averageRating },
                totalEarnings = user.professionalInfo.balance
            )
        }
    }
}

