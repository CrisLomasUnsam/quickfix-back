package quickfix.utils.exceptions

data class CustomerException(override val message : String) : Exception(message)