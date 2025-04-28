package quickfix.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import quickfix.dao.JobRepository
import quickfix.dao.RatingRepository
import quickfix.dao.UserRepository
import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.Job
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import java.time.LocalDate


@DataJpaTest
class JobServiceSpec {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var jobRepository: JobRepository
    @Autowired lateinit var ratingRepository: RatingRepository

    @BeforeEach
    fun setup() {
        jobRepository.deleteAll()
        ratingRepository.deleteAll()
    }

    @Test
    fun testFindRatingsByCustomerId() {}

    @Test
    fun testFindRatingsByProfessionalId() {}

    @Test
    fun testFindRatingsByCustomerIdWithNoRatings() {}

    @Test
    fun testFindRatingsByProfessionalIdWithNoRatings() {}

    @Test
    fun testGetEarningsByProfessionalIdAndDateRange() {

        val professional = User().apply {
            mail = "cris@example.com"
            name = "Cristina"
            lastName = "Palacios"
            password = "dummyPassword"
            dni = 12345679
            avatar = "imgCris2"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply {street = "Rafaela 5053"; city = "CABA"; zipCode = "1000"}
            verified = true
        }

        userRepository.save(professional)
        val professionalEntity = userRepository.findByDni(12345679)

        val job1 = Job().apply {
            id = 1L
            price = 100.0
            this.professional = professionalEntity!!
            status = JobStatus.DONE
            date = LocalDate.of(2024, 5, 1)
            initDateTime = LocalDate.now()
        }

        val job2 = Job().apply {
            id = 2L
            price = 200.0
            this.professional = professionalEntity!!
            status = JobStatus.DONE
            date = LocalDate.of(2024, 5, 5)
            initDateTime = LocalDate.now()
        }

        val job3 = Job().apply {
            id = 3L
            price = 300.0
            this.professional = professionalEntity!!
            status = JobStatus.PENDING
            date = LocalDate.of(2024, 5, 10)
            initDateTime = LocalDate.now()
        }

        jobRepository.saveAll(listOf(job1, job2, job3))
        val jobs = jobRepository.findAll()
        jobs.forEach {
            println("Job id=${it.id}, status=${it.status}, Date=${it.initDateTime}")
        }



        val startDate = LocalDate.of(2024, 5, 1)
        val endDate = LocalDate.of(2024, 5, 31)

        // When
        val earnings = jobRepository.getEarningsByProfessionalIdAndDateRange(1L, startDate, endDate)

        // Then
        assertEquals(300.0, earnings) // 100 + 200 = 300
    }
}