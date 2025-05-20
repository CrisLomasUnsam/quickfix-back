package quickfix.dto.register

data class NewCredentialRequestDTO(
    val newRawPassword: String,
    val token: String
)
