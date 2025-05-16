package quickfix.utils.exceptions

data class RatingException(override val message : String) : Exception(message)