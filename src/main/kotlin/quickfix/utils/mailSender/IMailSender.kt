package quickfix.utils.mailSender

interface IMailSender {
    fun sendEmail(mail: List<Mail>)
}