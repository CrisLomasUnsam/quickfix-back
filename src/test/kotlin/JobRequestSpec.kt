<<<<<<< HEAD
/*import io.kotest.assertions.throwables.shouldThrow
=======
/* import io.kotest.assertions.throwables.shouldThrow
>>>>>>> f450e0a3eeac795166b5d42bca2d021c7c6da197
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import mocks.createCustomerMock
import quickfix.dto.job.JobRequestDTO
import quickfix.dto.job.toJobRequest
import quickfix.models.Profession
import quickfix.utils.exceptions.BusinessException


class JobRequestSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    describe("JobRequestDTO"){
        val customer = createCustomerMock().customer
        val profession = Profession.GASISTA
        val detail = "Detalle"

        it("should convert to JobRequest") {

            val dto = JobRequestDTO(
                customer = customer,
                profession = profession,
                detail = detail
            )

            val job = dto.toJobRequest()

            job.customer shouldBe customer
            job.profession shouldBe profession
            job.detail shouldBe detail
        }

        it("should throw exception when detail is blank") {
            val dto = JobRequestDTO(
                customer = customer,
                profession = profession,
                detail = ""
            )

            val exception = shouldThrow<BusinessException> {
                dto.toJobRequest()
            }

            exception.message shouldBe "Detail is empty"
        }
    }
<<<<<<< HEAD
})*/
=======
}) */
>>>>>>> f450e0a3eeac795166b5d42bca2d021c7c6da197
