package quickfix.utils.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
data class BusinessException(val msg : String = "Ha habido un error al recuperar los datos.") : Exception(msg)