package quickfix.dto.user

import quickfix.models.User

data class UserProfileInfoDto (
    val name : String,
    val lastName: String,
    val avatar: String,
)
{
    companion object{
        fun toDTO(user : User) : UserProfileInfoDto{
            return UserProfileInfoDto(
                name = user.name,
                lastName = user.lastName,
                avatar = "holis",
            )
        }
    }
}