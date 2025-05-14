package quickfix.utils.exceptions

data class IllegalDataException (override val message : String) : Exception(message)