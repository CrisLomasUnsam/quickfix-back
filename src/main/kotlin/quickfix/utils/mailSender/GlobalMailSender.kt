package quickfix.utils.mailSender

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class GlobalMailSender(val mailSender: IMailSender){

    @EventListener
    fun handleAnyMailEvent(event: Any) {
        val strategy = MailFactory.getStrategyByEvent(event) ?: return
        val mail = strategy.generateMail(event)
        mailSender.sendEmail(mail)
    }
}