package mocks

import io.mockk.mockk
import quickfix.services.RegisterService
import quickfix.utils.mailSender.IMailSender
import quickfix.utils.mailSender.MailObserver

data class MailSenderMock (
    val mockedMailSender: IMailSender,
    val mockedRegisterService: RegisterService
)

fun createMailSenderMock (): MailSenderMock{
    val mockedMailSender = mockk<IMailSender>(relaxUnitFun = true)
    val mockedMailObserver = MailObserver(mailSender = mockedMailSender)
    val mockedRegisterService = RegisterService(mockedMailObserver)
    return MailSenderMock(mockedMailSender, mockedRegisterService)
}