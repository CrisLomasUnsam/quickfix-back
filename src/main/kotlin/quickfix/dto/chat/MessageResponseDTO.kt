package quickfix.dto.chat

data class MessageResponseDTO(
    val message: String,
    val itsMine: Boolean,
    val datetime: String
)