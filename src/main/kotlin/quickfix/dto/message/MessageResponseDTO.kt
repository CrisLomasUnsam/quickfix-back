package quickfix.dto.message

data class MessageResponseDTO(
    val message: String,
    val itsMine: Boolean,
    val datetime: String
)