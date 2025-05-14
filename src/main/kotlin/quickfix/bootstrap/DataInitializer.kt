package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.bootstrap.builders.*
import quickfix.dao.*
import quickfix.dto.job.jobRequest.JobRequestRedisDTO
import quickfix.services.RedisService

@Service
class DataInitializer : InitializingBean {

    @Autowired private lateinit var redisService: RedisService
    @Autowired private lateinit var ratingRepository: RatingRepository
    @Autowired private lateinit var jobRepository: JobRepository
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var professionRepository: ProfessionRepository

    override fun afterPropertiesSet() {
        initProfessions()
        initUsers()
        initJobs()
        loadJobRequestsToRedis()
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
        val job3 = JobBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, professions["Electricidad"]!!, done = true)
        val job4 = JobBuilder.buildMock(users["custom2"]!!, users["prof1"]!!, professions["Electricidad"]!!, done = true)
        val job5 = JobBuilder.buildMock(users["custom2"]!!, users["prof2"]!!, professions["Mecánica"]!!, done = true)
        val job6 = JobBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, professions["Albañilería"]!!, done = true)
        val job7 = JobBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, professions["Albañilería"]!!, done = true)

        jobRepository.saveAll(listOf(job1, job2, job3, job4, job5, job6, job7))

        val rating1 = RatingBuilder.buildMock(users["custom1"]!!, users["prof1"]!!, job1, score = 5)
        val rating2 = RatingBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, job2, score = 4)
        val rating3 = RatingBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, job3, score = 3)
        val rating4 = RatingBuilder.buildMock(users["custom2"]!!, users["prof1"]!!, job4, score = 4)
        val rating5 = RatingBuilder.buildMock(users["custom2"]!!, users["prof2"]!!, job5, score = 3)
        val rating6 = RatingBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, job6, score = 5)
        val rating7 = RatingBuilder.buildMock(users["custom3"]!!, users["prof1"]!!, job7, score = 4)
        val rating8 = RatingBuilder.buildMock(users["prof1"]!!, users["custom1"]!!, job1, score = 4)
        val rating9 = RatingBuilder.buildMock(users["prof2"]!!, users["custom1"]!!, job2, score = 5)
        val rating10 = RatingBuilder.buildMock(users["prof2"]!!, users["custom1"]!!, job3, score = 4)
        val rating11 = RatingBuilder.buildMock(users["prof1"]!!, users["custom2"]!!, job4, score = 3)
        val rating12 = RatingBuilder.buildMock(users["prof2"]!!, users["custom2"]!!, job5, score = 4)
        val rating13 = RatingBuilder.buildMock(users["prof1"]!!, users["custom3"]!!, job6, score = 3)
        val rating14 = RatingBuilder.buildMock(users["prof1"]!!, users["custom3"]!!, job7, score = 5)

        ratingRepository.saveAll(listOf(rating1, rating2, rating3, rating4, rating5, rating6, rating7, rating8, rating9, rating10, rating11, rating12, rating13, rating14))


    }

    fun loadJobRequestsToRedis() {

        redisService.cleanupJobRequest()

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val jobRequest1 = JobRequestRedisDTO(
            customerId = users["custom1"]!!.id,
            professionId = professions["Electricidad"]!!.id,
            detail = "Mi instalación eléctrica no está funcionando bien, necesito ayuda para repararla."
        )

        val jobRequest2 = JobRequestRedisDTO(
            customerId = users["custom2"]!!.id,
            professionId = professions["Mecánica"]!!.id,
            detail = "Se me quedó el auto en la puerta de mi casa."
        )

        val jobRequest3 = JobRequestRedisDTO(
            customerId = users["custom3"]!!.id,
            professionId = professions["Albañilería"]!!.id,
            detail = "Quiero hacer una remodelación en el baño, necesito un albañil para eso."
        )

        redisService.requestJob(jobRequest1, professions["Electricidad"]!!.id)
        redisService.requestJob(jobRequest2, professions["Mecánica"]!!.id)
        redisService.requestJob(jobRequest3, professions["Albañilería"]!!.id)
    }

}
