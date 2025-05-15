package quickfix.dto.chat

import quickfix.models.User

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
                avatar = "https://www.psicologiamadrid.es/wp-content/uploads/2020/02/personas-autoritarias.jpg"
            )
        }
    }
}