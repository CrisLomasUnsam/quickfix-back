package quickfix.utils.mailSender

const val FROM = "quickfix.app.noreply@gmail.com"

data class Mail(
    val from: String = FROM,
    var to: String,
    val subject: String,
    var content: String
) {
    companion object {

        fun toRegistrationMail(
            to: String,
            content: String

        ): Mail {
            return Mail(
                from = FROM,
                to = to,
                subject = "Confirme su cuenta",
                content = content
            )
        }

        fun toPasswordRecoveryMail(
            to: String,
            content: String

        ): Mail {
            return Mail(
                from = FROM,
                to = to,
                subject = "Recupere su contraseña",
                content = content
            )
        }
    }
}