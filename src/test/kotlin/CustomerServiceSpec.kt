/*import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import mocks.createCustomerMock
import quickfix.dao.CustomerRepository
import quickfix.dao.JobRepository
import quickfix.models.Job
import quickfix.services.CustomerService
import quickfix.utils.JobSearchParameters
import java.time.LocalDate

class CustomerServiceSpec: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val mockedJobRepository = mockk<JobRepository>()
    val mockedCustomerService = CustomerService(mockedJobRepository)
    val jobRepository = JobRepository(customerRepository = CustomerRepository())
    val customerService = CustomerService(jobRepository = jobRepository)

    describe("getJobsByParameters") {
        it("should return matching jobs from the repository") {
            val mockedCustomer = createCustomerMock().customer
            val job = Job().apply {
                customer = mockedCustomer
                this.done = true}

            val params = JobSearchParameters(done = true)

            every { mockedJobRepository.searchByParameters(job.customer.id, params) } returns listOf(job)

            val result = mockedCustomerService.getJobsByParameter(job.customer.id, params)

            result.size shouldBe  1
            result.first() shouldBe job
        }

        it("should return empty list when no jobs match the parameters") {
            val mockedCustomer = createCustomerMock().customer
            val job = Job().apply {
                customer = mockedCustomer
                this.done = false
            }

            val params = JobSearchParameters(done = true)

            every { mockedJobRepository.searchByParameters(job.customer.id, params) } returns emptyList()

            val result = mockedCustomerService.getJobsByParameter(job.customer.id, params)

            result.size shouldBe 0
        }
    }

    describe("searchParameters.matches") {
        it("should return false when parameters do not match") {
            val job = Job().apply {
                done = false
            }

            val params = JobSearchParameters(done = true)

            params.matches(job) shouldBe false
        }
    }

    describe("testDeIntegracion") {
        val customer1 = createCustomerMock().customer.apply { id = 1 }
        val customer2 = createCustomerMock().customer.apply { id = 2 }

        val job1 = Job().apply {
            customer = customer1
            done = true
            canceled = false
            date = LocalDate.now()
            price= 100.0
        }

        val job2 = Job().apply {
            customer = customer1
            done = true
            canceled = true
            date = LocalDate.now()
            price= 100.0
        }

        val job3 = Job().apply {
            customer = customer2
            done = true
            canceled = false
            date = LocalDate.now()
            price= 100.0
        }

        beforeTest {
            jobRepository.create(job1)
            jobRepository.create(job2)
            jobRepository.create(job3)
        }

        it("should return 2 done jobs for customer 1") {
            val params = JobSearchParameters(done = true)

            val result = customerService.getJobsByParameter(customer1.id, params)

            result.size shouldBe  2
        }

        it("should return empty list when no jobs match the parameters") {
            val params = JobSearchParameters(done = false)

            val result = customerService.getJobsByParameter(customer1.id, params)

            result.size shouldBe 0
        }

    }

    afterTest {
        jobRepository.clearAll()
    }
})*/