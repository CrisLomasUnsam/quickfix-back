package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.bootstrap.builders.*
import quickfix.dao.*

@Service
class DataInitializer : InitializingBean {

    @Autowired private lateinit var ratingRepository: RatingRepository
    @Autowired private lateinit var jobRepository: JobRepository
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var professionRepository: ProfessionRepository

    override fun afterPropertiesSet() {
        initProfessions()
        initUsers()
        initJobs()
    }

    fun initProfessions() {
        professionRepository.saveAll(ProfessionBuilder.buildProfessions())
    }

    fun initUsers() {
        val professions = professionRepository.findAll().toList()

        val prof1 = ProfessionalBuilder.buildMock("prof1", professions)
        val prof2 = ProfessionalBuilder.buildMock("prof2", professions)
        val prof3 = ProfessionalBuilder.buildMock("prof3", professions)
        val custom1 = CustomerBuilder.buildMock("custom1")
        val custom2 = CustomerBuilder.buildMock("custom2")
        val custom3 = CustomerBuilder.buildMock("custom3")

        userRepository.saveAll(listOf(custom1, custom2, custom3, prof1, prof2, prof3))
    }

    fun initJobs() {

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val job1 = JobBuilder.buildMock(users["custom1"]!!, users["prof1"]!!, professions["Plomería"]!!, done = true)
        val job2 = JobBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, professions["Albañilería"]!!, done = true)
        val job3 = JobBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, professions["Electricidad"]!!, done = false)
        val job4 = JobBuilder.buildMock(users["custom2"]!!, users["prof1"]!!, professions["Electricidad"]!!, done = true)
        val job5 = JobBuilder.buildMock(users["custom2"]!!, users["prof3"]!!, professions["Gasfitería"]!!, done = false)
        val job6 = JobBuilder.buildMock(users["custom3"]!!, users["prof3"]!!, professions["Carpintería"]!!, done = false)
        val job7 = JobBuilder.buildMock(users["custom3"]!!, users["prof2"]!!, professions["Pintorería"]!!, done = false)
        val job8 = JobBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, professions["Jardinería"]!!, done = false)
        val job9 = JobBuilder.buildMock(users["custom1"]!!, users["prof3"]!!, professions["Arquitectura"]!!, done = false)
        val job10 = JobBuilder.buildMock(users["custom2"]!!, users["prof2"]!!, professions["Mecánica"]!!, done = false)
        val job11 = JobBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, professions["Albañilería"]!!, done = true)
        val job12 = JobBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, professions["Albañilería"]!!, done = true)

        jobRepository.saveAll(listOf(job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12))

        val rating1 = RatingBuilder.buildMock(users["custom1"]!!, users["prof1"]!!, job1, 5)
        val rating2 = RatingBuilder.buildMock(users["prof1"]!!, users["custom1"]!!, job1, 4)
        val rating3 = RatingBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, job2, 3)
        val rating4 = RatingBuilder.buildMock(users["prof2"]!!, users["custom1"]!!, job2, 1)
        val rating5 = RatingBuilder.buildMock(users["custom2"]!!, users["prof1"]!!, job4, 4)
        val rating6 = RatingBuilder.buildMock(users["prof1"]!!, users["custom2"]!!, job4, 4)
        val rating7 = RatingBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, job11, 5)
        val rating8 = RatingBuilder.buildMock(users["prof1"]!!, users["custom3"]!!, job11, 3)
        val rating9 = RatingBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, job12, 4)
        val rating10 = RatingBuilder.buildMock(users["prof1"]!!, users["custom3"]!!, job12, 4)


        ratingRepository.saveAll(listOf(rating1, rating2, rating3, rating4, rating5, rating6, rating7, rating8, rating9, rating10))


    }
}
