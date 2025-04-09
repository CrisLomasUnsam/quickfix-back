import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import mocks.createCustomerUserMock
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.job.toJobRequest
import quickfix.models.Profession
import quickfix.utils.exceptions.BusinessException


class JobRequestSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("JobRequestDTO"){
        val customerUser = createCustomerUserMock().customerUser
        val profession = Profession.GASISTA
        val detail = "Detalle"

        it("should convert to JobRequest") {

            val dto = JobRequestDTO(
                customer = customerUser,
                profession = profession,
                detail = detail
            )

            val job = dto.toJobRequest()

            job.customer shouldBe customerUser
            job.profession shouldBe profession
            job.detail shouldBe detail
        }

        it("should throw exception when detail is blank") {
            val dto = JobRequestDTO(
                customer = customerUser,
                profession = profession,
                detail = ""
            )

            val exception = shouldThrow<BusinessException> {
                dto.toJobRequest()
            }

            exception.message shouldBe "Detalle está vacío"
        }
    }
})