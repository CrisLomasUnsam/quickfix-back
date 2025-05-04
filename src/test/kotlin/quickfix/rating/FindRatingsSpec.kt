package quickfix.rating

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import quickfix.dao.JobRepository
import quickfix.dao.ProfessionRepository
import quickfix.dao.RatingRepository
import quickfix.dao.UserRepository
import quickfix.mock.createCustomerMock
import quickfix.mock.createJobMock
import quickfix.mock.createProfessionalMock
import quickfix.mock.createRatingMock
import quickfix.models.Profession
import java.time.LocalDate

@DataJpaTest
class RatingPaginationTest {

    @Autowired private lateinit var professionRepository: ProfessionRepository
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var jobRepository: JobRepository
    @Autowired private lateinit var ratingRepository: RatingRepository

    @BeforeEach
    fun setup() {
        ratingRepository.deleteAll()
        jobRepository.deleteAll()
        userRepository.deleteAll()
        professionRepository.deleteAll()
    }

    @Test
    fun testFindRatingsRecibidosPorUsuario() {

        professionRepository.save(Profession().apply { name = "Electricista" })

        val profession = professionRepository.findByNameIgnoreCase("Electricista").orElseThrow()

        val (customer, profesional) = userRepository.saveAll(
            listOf(
                createCustomerMock().customerUser,
                createProfessionalMock().professionalUser
            )
        ).toList()

        val (job1, job2, job3, job4) = jobRepository.saveAll(
            listOf(
                createJobMock(customer, profesional, profession, 200.0).job,
                createJobMock(customer, profesional, profession, 300.0).job,
                createJobMock(customer, profesional, profession, 400.0).job,
                createJobMock(customer, profesional, profession, 100.0).job
            )
        ).toList()

        /* EL USUARIO ES EVALUADO */
        val ratings = ratingRepository.saveAll(
            listOf(
                createRatingMock(profesional, customer, job1, 5, LocalDate.now().minusMonths(1)).rating,
                createRatingMock(profesional, customer, job2, 3, LocalDate.now()).rating,
                createRatingMock(profesional, customer, job3, 1, LocalDate.now()).rating,
                createRatingMock(profesional, customer, job4, 4, LocalDate.now()).rating
            )
        ).toList()

        // Act: solicitar primer page con 2 ratings
        val pageRequest = PageRequest.of(0,2, Sort.Direction.ASC, "yearAndMonth")
        val response = ratingRepository.findByUserToId(customer.id, pageRequest)

        // Assert
        assertEquals(2, response.content.size)
        assertEquals(4, response.totalElements)
        assertEquals(ratings.sortedBy { it.yearAndMonth }[0].id, response.content[0].id)
    }

    @Test
    fun testFindRatingsRealizadoPorUsuario() {

        professionRepository.save(Profession().apply { name = "Gasista" })

        val profession = professionRepository.findByNameIgnoreCase("Gasista").orElseThrow()

        val (customer, profesional) = userRepository.saveAll(
            listOf(
                createCustomerMock().customerUser,
                createProfessionalMock().professionalUser
            )
        ).toList()

        val (job1, job2) = jobRepository.saveAll(
            listOf(
                createJobMock(customer, profesional, profession, 200.0).job,
                createJobMock(customer, profesional, profession, 300.0).job
            )
        ).toList()

        /* EL USUARIO EVALÚA */
        val ratings = ratingRepository.saveAll(
            listOf(
                createRatingMock(customer, profesional, job1, 5, LocalDate.now().minusMonths(1)).rating,
                createRatingMock(customer, profesional, job2, 3, LocalDate.now()).rating
            )
        ).toList()

        // Act: solicitar primer page con 1 rating
        val pageRequest = PageRequest.of(0,1, Sort.Direction.ASC, "yearAndMonth")
        val response = ratingRepository.findByUserFromId(customer.id, pageRequest)

        // Assert
        assertEquals(1, response.content.size)
        assertEquals(2, response.totalElements)
        assertEquals(ratings.sortedBy { it.yearAndMonth }[0].id, response.content[0].id)
    }
}