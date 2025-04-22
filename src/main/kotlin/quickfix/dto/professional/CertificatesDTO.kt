package quickfix.dto.professional

data class CertificateDTO(
    val profession: String,
    val imgs: MutableSet<String>
)
