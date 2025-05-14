package quickfix.utils.exceptions

data class ProfessionalException(override val message : String) : Exception(message)