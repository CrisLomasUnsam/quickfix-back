package quickfix.utils.exceptions

data class DetailException(override val message : String) : Exception(message)