package mocks

<<<<<<< HEAD
/*import io.mockk.mockk
=======
import io.mockk.mockk
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository
import quickfix.dao.UserInfoRepository
>>>>>>> f450e0a3eeac795166b5d42bca2d021c7c6da197
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
    val mockedCustomerRepository = mockk<CustomerRepository>(relaxUnitFun = true)
    val mockedProffesionalRepository = mockk<ProfessionalRepository>(relaxUnitFun = true)
    val mockedUserInfoRepository = mockk<UserInfoRepository>(relaxUnitFun = true)
    val mockedRegisterService = RegisterService(mockedMailObserver,mockedUserInfoRepository,mockedCustomerRepository,mockedProffesionalRepository,)
    return MailSenderMock(mockedMailSender, mockedRegisterService)
}*/