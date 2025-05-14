package quickfix.utils.exceptions

data class InvalidCredentialsException (override val message : String = "Credenciales inválidas") : Exception(message)