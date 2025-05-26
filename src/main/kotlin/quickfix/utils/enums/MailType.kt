package quickfix.utils.enums

enum class MailType(val subject: String) {
    REGISTRATION_CONFIRMATION("Confirme su cuenta"),
    PASSWORD_RECOVERY("Recupere su contraseña"),
    JOB_REQUESTED("Ha solicitado un trabajo"),
    JOB_OFFERED("Ha ofertado en un trabajo"),
    JOB_STARTED("Trabajo iniciado"),
    JOB_ENDED("Trabajo finalizado"),
    JOB_CANCELED("Trabajo cancelado"),
    REVIEW_LEFT("Nueva calificación recibida"),

}