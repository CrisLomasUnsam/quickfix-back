package quickfix.utils.mailSender

data class Mail(
    val from: String = "quickfix.app.noreply@gmail.com",
    var to: String,
    val subject: String = "Confirme su cuenta",
    var content: String
)