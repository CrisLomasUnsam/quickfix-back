import quickfix.dto.register.RegisterRequestDTO
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.verify
import mocks.createMailSenderMock
import quickfix.models.Address
import quickfix.models.Gender
import quickfix.utils.DateWithDayFormatter
import quickfix.utils.mailSender.*
import quickfix.utils.stringifyDate
import java.time.LocalDate


class MailSenderSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("given an email address") {
        val (mockedMailSender, mockedRegisterService, mockedUserRepository) = createMailSenderMock()

        it("sends the registration email if it's valid") {
            val address = "valid@user.com"
            val mail = Mail(to = address)
            mockedMailSender.sendEmail(mail)

            verify(exactly = 1) {
                mockedMailSender.sendEmail(
                    withArg {
                        it.from shouldBe "registration@quickfix.com"
                        it.to shouldBe "valid@user.com"
                        it.subject shouldBe "Confirm your account"
                        it.content shouldBe "Confirm your account by clicking on the link below"
                    }
                )
            }
        }

        it("doesn't send the registration email to a random address") {
            val address = "other@user.com"
            val mail = Mail(to = address)
            mockedMailSender.sendEmail(mail)

            verify(exactly = 1) {
                mockedMailSender.sendEmail(
                    withArg {
                        it.from shouldBe "registration@quickfix.com"
                        it.to shouldNotBe "valid@user.com"
                        it.subject shouldBe "Confirm your account"
                        it.content shouldBe "Confirm your account by clicking on the link below"
                    }
                )
            }
        }

        it("doesn't send the registration email to an invalid address") {
            val invalidAddress = ""
            val registerData = RegisterRequestDTO(
                rawPassword = "123",
                mail = invalidAddress,
                name = "Carlos",
                lastName = "Perez",
                dni = 11222333,
                avatar = "...",
                dateBirth = stringifyDate(LocalDate.of(2001,1, 1), DateWithDayFormatter),
                gender = Gender.MALE,
                address = Address().apply {
                    street = "Avenida siempre viva 123"
                    city = "Springfield"
                    zipCode = "01234"
                }
            )
            every { mockedUserRepository.findByMail(any()) } returns null
            try {
                mockedRegisterService.registerUser(registerData)
                throw AssertionError("Exception should have been thrown for invalid data.")
            } catch (e: Exception) {
                e.message shouldBe "El email no es válido."
            }
            verify(exactly = 0) { mockedMailSender.sendEmail(any()) }
        }
    }
})