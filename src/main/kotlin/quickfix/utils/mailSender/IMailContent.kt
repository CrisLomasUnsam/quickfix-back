package quickfix.utils.mailSender

interface IMailContent {
    fun generateMail(event: Any): Mail
}