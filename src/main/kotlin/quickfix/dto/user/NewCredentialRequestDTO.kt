package quickfix.dto.user

data class NewCredentialRequestDTO(
    val newRawPassword: String,
    val token: String
)
