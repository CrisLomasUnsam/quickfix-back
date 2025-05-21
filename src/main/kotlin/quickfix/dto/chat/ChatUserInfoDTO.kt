package quickfix.dto.chat

import quickfix.models.User
import quickfix.utils.functions.getAvatarUrl

data class ChatUserInfoDTO(
    val name: String,
    val lastName: String,
    val avatar: String
){

    companion object {
        fun toDTO(user: User): ChatUserInfoDTO{
            return ChatUserInfoDTO(
                name =  user.name,
                lastName = user.lastName,
                avatar = getAvatarUrl(user.id)
            )
        }
    }
}