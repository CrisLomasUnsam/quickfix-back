package mocks

import quickfix.dto.register.RegisterRequestDTO
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import io.mockk.verify
import quickfix.utils.mailSender.Mail
import quickfix.utils.mailSender.MailObserver
import quickfix.utils.mailSender.MailSender
import quickfix.services.RegisterService
import utils.mailSender.*


class MailSenderSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("given an email address") {
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
        val mailObserver = MailObserver(mailSender = mockedMailSender)
        val registerService = RegisterService(mailObserver)

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
            val registerData = RegisterRequestDTO(password = "123", email = invalidAddress)
            try {
                registerService.registerCustomer(registerData)
                throw AssertionError("Exception should have been thrown for invalid data.")
            } catch (e: Exception) {
                e.message shouldBe "Invalid data"
            }
            verify(exactly = 0) { mockedMailSender.sendEmail(any()) }
        }
    }
})