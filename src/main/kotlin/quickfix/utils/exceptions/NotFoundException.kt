package quickfix.utils.exceptions

data class NotFoundException(override val message: String = "Ha habido un error al recuperar los datos") : Exception(message)