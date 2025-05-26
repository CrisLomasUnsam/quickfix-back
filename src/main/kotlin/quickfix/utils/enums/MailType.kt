package quickfix.utils.enums

enum class MailType(val subject: String) {
    REGISTRATION_CONFIRMATION("Confirme su cuenta"),
    PASSWORD_RECOVERY("Recupere su contraseña"),
    JOB_REQUESTED("Ha solicitado un trabajo"),
    JOB_OFFERED("Ha ofertado en un trabajo"),
    JOB_ACCEPTED("Han aceptado tu oferta"),
    JOB_STARTED("Trabajo iniciado"),
    JOB_DONE("Trabajo finalizado exitosamente"),
    JOB_CANCELED("Trabajo cancelado"),
    RATING_RECEIVED("Nueva calificación recibida"),
    RATING_EDITED("Calificación editada"),
    DEBT_PAID("Estado de deuda"),
    INFO_EDITED("Datos actualizados")
}