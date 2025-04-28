package quickfix.mock
/*import io.mockk.mockk
import org.springframework.security.crypto.password.PasswordEncoder
import quickfix.dao.UserRepository
import quickfix.services.RegisterService
import quickfix.utils.mailSender.IMailSender
import quickfix.utils.mailSender.MailObserver

data class MailSenderMock(
    val mockedMailSender: IMailSender,
    val mockedRegisterService: RegisterService,
    val mockedUserRepository: UserRepository
)

fun createMailSenderMock (): MailSenderMock{
    val mockedMailSender = mockk<IMailSender>(relaxUnitFun = true)
    val mockedMailObserver = MailObserver(mailSender = mockedMailSender)
    val mockedUserRepository = mockk<UserRepository>(relaxUnitFun = true)
    val mockedPasswordEncoder = mockk<PasswordEncoder>(relaxUnitFun = true)
    val mockedRegisterService = RegisterService(mockedMailObserver,mockedUserRepository,mockedPasswordEncoder)
    return MailSenderMock(mockedMailSender, mockedRegisterService, mockedUserRepository)
}*/