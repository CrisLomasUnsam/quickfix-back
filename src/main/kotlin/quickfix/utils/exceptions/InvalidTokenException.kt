package quickfix.utils.exceptions

data class InvalidTokenException(override val message: String = "Token inválido") : Exception(message)