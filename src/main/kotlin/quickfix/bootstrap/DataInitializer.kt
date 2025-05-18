package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.bootstrap.builders.*
import quickfix.dao.*
import quickfix.services.JobService
import quickfix.services.RedisService

@Service
class DataInitializer : InitializingBean {

    @Autowired private lateinit var jobService: JobService
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
        val tester = CustomerBuilder.buildMock("tester").apply { this.mail = "alt.gm-0okdbotm@yopmail.com" }


        userRepository.saveAll(listOf(
            custom1, custom2, custom3, custom4, custom5, custom6, custom7, custom8, custom9, tester,
            prof1, prof2, prof3)
        )
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

        val rating1 = RatingBuilder.buildMock(users["custom1"]!!, job1, score = 5)
        val rating2 = RatingBuilder.buildMock(users["custom1"]!!, job2, score = 4)
        val rating3 = RatingBuilder.buildMock(users["custom1"]!!, job3, score = 3)
        val rating4 = RatingBuilder.buildMock(users["custom2"]!!, job4, score = 4)
        val rating5 = RatingBuilder.buildMock(users["custom2"]!!, job5, score = 3)
        val rating6 = RatingBuilder.buildMock(users["custom3"]!!, job6, score = 5)
        val rating7 = RatingBuilder.buildMock(users["custom3"]!!, job7, score = 4)
        val rating8 = RatingBuilder.buildMock(users["prof1"]!!, job1, score = 4)
        val rating9 = RatingBuilder.buildMock(users["prof2"]!!, job2, score = 5)
        val rating10 = RatingBuilder.buildMock(users["prof2"]!!, job3, score = 4)
        val rating11 = RatingBuilder.buildMock(users["prof1"]!!, job4, score = 3)
        val rating12 = RatingBuilder.buildMock(users["prof2"]!!, job5, score = 4)
        val rating13 = RatingBuilder.buildMock(users["prof1"]!!, job6, score = 3)
        val rating14 = RatingBuilder.buildMock(users["prof1"]!!, job7, score = 5)
        val rating15 = RatingBuilder.buildMock(users["prof2"]!!, job8, score = 4)
        val rating16 = RatingBuilder.buildMock(users["prof1"]!!, job9, score = 5)
        val rating17 = RatingBuilder.buildMock(users["prof2"]!!, job10, score = 3)
        val rating18 = RatingBuilder.buildMock(users["prof1"]!!, job11, score = 1)
        val rating19 = RatingBuilder.buildMock(users["prof2"]!!, job12, score = 2)
        val rating20 = RatingBuilder.buildMock(users["prof1"]!!, job13, score = 3)

        ratingRepository.saveAll(
            listOf(rating1, rating2, rating3, rating4, rating5, rating6, rating7, rating8, rating9, rating10,
            rating11, rating12, rating13, rating14, rating15, rating16, rating17, rating18, rating19, rating20, rating20)
        )
    }

    fun loadJobRequestsToRedis() {

        redisService.cleanupJobRequestsForTesting()

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val jobRequest1 = JobRequestBuilder.buildMock(users["custom1"]!!, professions["Electricidad"]!!, isInstantRequest = true)
        val jobRequest2 = JobRequestBuilder.buildMock(users["custom2"]!!, professions["Mecánica"]!!)
        val jobRequest3 = JobRequestBuilder.buildMock(users["custom3"]!!, professions["Albañilería"]!!)
        val jobRequest4 = JobRequestBuilder.buildMock(users["custom4"]!!, professions["Plomería"]!!)
        val jobRequest5 = JobRequestBuilder.buildMock(users["custom5"]!!, professions["Carpintería"]!!)
        val jobRequest6 = JobRequestBuilder.buildMock(users["custom6"]!!, professions["Jardinería"]!!)
        val jobRequest7 = JobRequestBuilder.buildMock(users["custom7"]!!, professions["Pintorería"]!!)
        val jobRequest8 = JobRequestBuilder.buildMock(users["custom8"]!!, professions["Gasfitería"]!!)
        val jobRequest9 = JobRequestBuilder.buildMock(users["custom9"]!!, professions["Plomería"]!!)

        val jobRequest10 = JobRequestBuilder.buildMock(users["custom1"]!!, professions["Mecánica"]!!)
        val jobRequest11 = JobRequestBuilder.buildMock(users["custom1"]!!, professions["Albañilería"]!!)
        val jobRequest12 = JobRequestBuilder.buildMock(users["custom1"]!!, professions["Plomería"]!!)
        val jobRequest13 = JobRequestBuilder.buildMock(users["custom1"]!!, professions["Carpintería"]!!)
        val jobRequest14 = JobRequestBuilder.buildMock(users["custom1"]!!, professions["Jardinería"]!!)
        val jobRequest15 = JobRequestBuilder.buildMock(users["custom1"]!!, professions["Pintorería"]!!)

        jobService.requestJob(jobRequest1)
        jobService.requestJob(jobRequest2)
        jobService.requestJob(jobRequest3)
        jobService.requestJob(jobRequest4)
        jobService.requestJob(jobRequest5)
        jobService.requestJob(jobRequest6)
        jobService.requestJob(jobRequest7)
        jobService.requestJob(jobRequest8)
        jobService.requestJob(jobRequest9)
        jobService.requestJob(jobRequest10)
        jobService.requestJob(jobRequest11)
        jobService.requestJob(jobRequest12)
        jobService.requestJob(jobRequest13)
        jobService.requestJob(jobRequest14)
        jobService.requestJob(jobRequest15)

    }

}
