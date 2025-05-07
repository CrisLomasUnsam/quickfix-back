package quickfix.job
/*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import quickfix.dao.JobRepository
import quickfix.dao.UserRepository
import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.Job
import quickfix.models.User
import quickfix.utils.enums.JobStatus
import java.time.LocalDate
import kotlin.test.AfterTest

@DataJpaTest
class UserQuerySpec {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var jobRepository: JobRepository

    @BeforeEach
    fun setup() {
        userRepository.deleteAll()
        jobRepository.deleteAll()
    }

    @AfterTest
    fun cleanup() {
        userRepository.deleteAll()
        jobRepository.deleteAll()
    }

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

        Thread.sleep(2000)

        val professionalEntity = userRepository.findByDni(12345679)

        jobRepository.save(Job().apply {
            id = 1L
            price = 100.0
            this.professional = professionalEntity!!
            status = JobStatus.DONE
            date = LocalDate.of(2024, 5, 1)
            initDateTime = LocalDate.now()
        })

        jobRepository.save(Job().apply {
            id = 2L
            price = 200.0
            this.professional = professionalEntity!!
            status = JobStatus.DONE
            date = LocalDate.of(2024, 5, 5)
            initDateTime = LocalDate.now()
        })

        Thread.sleep(2000)

        val startDate = LocalDate.of(2024, 5, 1)
        val endDate = LocalDate.of(2024, 5, 31)
        // When
        val earnings = userRepository.getEarningsByProfessionalIdAndDateRange(1L, startDate, endDate)
        // Then
        assertEquals(300.0, earnings) /* 100 + 200 = 300 */
    }
}*/