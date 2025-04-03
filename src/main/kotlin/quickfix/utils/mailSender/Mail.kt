package quickfix.utils.mailSender

data class Mail(
    val from: String = "registration@quickfix.com",
    var to: String,
    val subject: String = "Confirm your account",
    var content: String = "Confirm your account by clicking on the link below",
)