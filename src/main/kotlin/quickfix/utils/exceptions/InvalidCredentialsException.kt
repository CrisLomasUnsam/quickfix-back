package quickfix.utils.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidCredentialsException (val msg : String = "Credenciales incorrectas.") : Exception(msg)