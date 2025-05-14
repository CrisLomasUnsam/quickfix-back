package quickfix.utils.exceptions

data class JobException(override val message : String) : Exception(message)