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
        val custom4 = CustomerBuilder.buildMock("custom4")
        val custom5 = CustomerBuilder.buildMock("custom5")
        val custom6 = CustomerBuilder.buildMock("custom6")
        val custom7 = CustomerBuilder.buildMock("custom7")
        val custom8 = CustomerBuilder.buildMock("custom8")
        val custom9 = CustomerBuilder.buildMock("custom9")


        userRepository.saveAll(listOf(
            custom1, custom2, custom3, custom4, custom5, custom6, custom7, custom8, custom9,
            prof1, prof2, prof3))
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
        val job8 = JobBuilder.buildMock(users["custom4"]!!, users["prof2"]!!, professions["Plomería"]!!, done = true)
        val job9 = JobBuilder.buildMock(users["custom5"]!!, users["prof1"]!!, professions["Carpintería"]!!, done = true)
        val job10 = JobBuilder.buildMock(users["custom6"]!!, users["prof2"]!!, professions["Jardinería"]!!, done = true)
        val job11 = JobBuilder.buildMock(users["custom7"]!!, users["prof1"]!!, professions["Pintorería"]!!, done = true)
        val job12 = JobBuilder.buildMock(users["custom8"]!!, users["prof2"]!!, professions["Gasfitería"]!!, done = true)
        val job13 = JobBuilder.buildMock(users["custom9"]!!, users["prof1"]!!, professions["Plomería"]!!, done = true)


        jobRepository.saveAll(listOf(job1, job2, job3, job4, job5, job6, job7, job8, job9, job10, job11, job12, job13))

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
        val rating15 = RatingBuilder.buildMock(users["prof2"]!!, users["custom4"]!!, job8, score = 4)
        val rating16 = RatingBuilder.buildMock(users["prof1"]!!, users["custom5"]!!, job9, score = 5)
        val rating17 = RatingBuilder.buildMock(users["prof2"]!!, users["custom6"]!!, job10, score = 3)
        val rating18 = RatingBuilder.buildMock(users["prof1"]!!, users["custom7"]!!, job11, score = 5)
        val rating19 = RatingBuilder.buildMock(users["prof2"]!!, users["custom8"]!!, job12, score = 4)
        val rating20 = RatingBuilder.buildMock(users["prof1"]!!, users["custom9"]!!, job13, score = 5)


        ratingRepository.saveAll(listOf(rating1, rating2, rating3, rating4, rating5, rating6, rating7, rating8, rating9, rating10,
            rating11, rating12, rating13, rating14, rating15, rating16, rating17, rating18, rating19, rating20, rating20))

    }

    fun loadJobRequestsToRedis() {

        redisService.cleanupJobRequest()

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val jobRequest1 = JobRequestRedisDTO(
            customerId = users["custom1"]!!.id,
            professionId = professions["Electricidad"]!!.id,
            detail = "Pasaron 3 meses, probé el disyuntor y no anda. Necesito cambiarlo."
        )

        val jobRequest2 = JobRequestRedisDTO(
            customerId = users["custom2"]!!.id,
            professionId = professions["Mecánica"]!!.id,
            detail = "Se me quedó el auto en la puerta de mi casa."
        )

        val jobRequest3 = JobRequestRedisDTO(
            customerId = users["custom3"]!!.id,
            professionId = professions["Albañilería"]!!.id,
            detail = "Quiero reparar un techo de durlock."
        )

        val jobRequest4 = JobRequestRedisDTO(
            customerId = users["custom4"]!!.id,
            professionId = professions["Plomería"]!!.id,
            detail = "Tengo una pérdida de agua debajo del fregadero, necesito que alguien lo revise."
        )

        val jobRequest5 = JobRequestRedisDTO(
            customerId = users["custom5"]!!.id,
            professionId = professions["Carpintería"]!!.id,
            detail = "Quiero montar un par de estantes."
        )

        val jobRequest6 = JobRequestRedisDTO(
            customerId = users["custom6"]!!.id,
            professionId = professions["Jardinería"]!!.id,
            detail = "Necesito que me corten el pasto y poden los árboles del fondo."
        )

        val jobRequest7 = JobRequestRedisDTO(
            customerId = users["custom7"]!!.id,
            professionId = professions["Pintorería"]!!.id,
            detail = "Quiero pintar el frente de la casa, incluyendo rejas y persianas."
        )

        val jobRequest8 = JobRequestRedisDTO(
            customerId = users["custom8"]!!.id,
            professionId = professions["Gasfitería"]!!.id,
            detail = "Necesito conectar un horno nuevo."
        )

        val jobRequest9 = JobRequestRedisDTO(
            customerId = users["custom9"]!!.id,
            professionId = professions["Plomería"]!!.id,
            detail = "Tengo una pérdida en mi baño."
        )

        redisService.requestJob(jobRequest1, professions["Electricidad"]!!.id)
        redisService.requestJob(jobRequest2, professions["Mecánica"]!!.id)
        redisService.requestJob(jobRequest3, professions["Albañilería"]!!.id)
        redisService.requestJob(jobRequest4, professions["Plomería"]!!.id)
        redisService.requestJob(jobRequest5, professions["Carpintería"]!!.id)
        redisService.requestJob(jobRequest6, professions["Jardinería"]!!.id)
        redisService.requestJob(jobRequest7, professions["Pintorería"]!!.id)
        redisService.requestJob(jobRequest8, professions["Gasfitería"]!!.id)
        redisService.requestJob(jobRequest9, professions["Plomería"]!!.id)

    }

}
