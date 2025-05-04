package quickfix.job

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import quickfix.dao.JobRepository
import quickfix.dao.ProfessionRepository
import quickfix.dao.RatingRepository
import quickfix.dao.UserRepository
import quickfix.mock.createCustomerMock
import quickfix.mock.createJobMock
import quickfix.mock.createProfessionalMock
import quickfix.mock.createRatingMock
import quickfix.models.Profession
import quickfix.models.Rating
import java.time.LocalDate


@DataJpaTest
class JobQuerySpec {

    @Autowired private lateinit var professionRepository: ProfessionRepository
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var jobRepository: JobRepository
    @Autowired private lateinit var ratingRepository: RatingRepository

    @PersistenceContext lateinit var entityManager: EntityManager

    @BeforeEach
    fun setup() {
        ratingRepository.deleteAll()
        jobRepository.deleteAll()
        userRepository.deleteAll()
        professionRepository.deleteAll()
    }

    @AfterEach
    fun cleanup(){
        ratingRepository.deleteAll()
        jobRepository.deleteAll()
        userRepository.deleteAll()
        professionRepository.deleteAll()
    }

    @Test
    fun testFindRatingsByCustomerId() {

        val customer = createCustomerMock().customerUser
        val professional = createProfessionalMock().professionalUser
        val profession = Profession().apply { name = "Electricista" }

        val profesionPersistida = professionRepository.save(profession)
        val customerPersistido = userRepository.save(customer)
        val profesionalPersistido = userRepository.save(professional)

        val job = createJobMock(customerPersistido, profesionalPersistido, profesionPersistida, 200.0).job
        val jobPersistido = jobRepository.save(job)

        /* el profesional evalúa al cliente */
        val rating = createRatingMock(profesionalPersistido, customerPersistido, jobPersistido, 5, LocalDate.now()).rating
        ratingRepository.save(rating)

        val ratings = entityManager.
        createNativeQuery("""
            SELECT r.* FROM ratings r
            JOIN jobs j ON j.id = r.job_id
            WHERE r.user_to_id = :id AND j.customer_id = :id
        """, Rating::class.java)
            .setParameter("id", customerPersistido.id)
            .resultList

        //val result = jobRepository.findRatingsByCustomerId(customerPersistido.id) ?: throw BusinessException("TEST FALLO")

        // Asserts
        assertEquals(1, ratings.size)
        assertTrue(ratings.first() is Rating)

        //assertEquals(5, ratings.first().score)
        //assertEquals("Excelente", ratings.first().comment)
    }

    @Test
    fun testFindRatingsByProfessionalId() {

        val customer = createCustomerMock().customerUser
        val professional = createProfessionalMock().professionalUser
        val profession = Profession().apply { name = "Gasista" }

        val profesionPersistida = professionRepository.save(profession)
        val customerPersistido = userRepository.save(customer)
        val profesionalPersistido = userRepository.save(professional)

        val job = createJobMock(customerPersistido, profesionalPersistido, profesionPersistida, 200.0).job
        val jobPersistido = jobRepository.save(job)

        /* el cliente evalúa al profesional */
        val rating = createRatingMock(customerPersistido, profesionalPersistido, jobPersistido, 5, LocalDate.now()).rating
        ratingRepository.save(rating)

        val ratings = entityManager
            .createNativeQuery("""
            SELECT r.* FROM ratings r
            JOIN jobs j ON j.id = r.job_id
            WHERE r.user_to_id = :id AND j.professional_id = :id
            """, Rating::class.java
            )
            .setParameter("id", profesionalPersistido.id)
            .resultList

        // Asserts
        assertEquals(1, ratings.size)
        assertTrue(ratings.first() is Rating)
        //assertEquals(5, result.first().score)
        //assertEquals("Excelente", result.first().comment)
    }
}