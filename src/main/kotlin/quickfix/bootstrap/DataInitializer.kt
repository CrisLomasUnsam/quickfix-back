package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.bootstrap.builders.*
import quickfix.dao.*
import quickfix.services.JobService
import quickfix.services.RatingService
import quickfix.services.RedisService

@Service
class DataInitializer : InitializingBean {

    @Autowired private lateinit var jobService: JobService
    @Autowired private lateinit var redisService: RedisService
    @Autowired private lateinit var ratingService: RatingService
    @Autowired private lateinit var jobRepository: JobRepository
    @Autowired private lateinit var userRepository: UserRepository
    @Autowired private lateinit var professionRepository: ProfessionRepository

    override fun afterPropertiesSet() {
        initProfessions()
        initUsers()
        initJobs()
        initRatings()
        loadJobRequestsToRedis()
        loadJobOffersToRedis()
    }

    fun initProfessions() {
        professionRepository.saveAll(ProfessionBuilder.buildProfessions())
    }

    fun initUsers() {
        val professions = professionRepository.findAll().toList()

        val prof1 = ProfessionalBuilder.buildMock("Mariano", "Cristobo", professions)
        val prof2 = ProfessionalBuilder.buildMock("Pablo", "Nuñez Monzon", professions)

        val custom1 = CustomerBuilder.buildMock("Valentino", "Bortolussi")
        val custom2 = CustomerBuilder.buildMock("Tomas", "Neiro")

        val tester = CustomerBuilder.buildMock("tester").apply { this.mail = "alt.gm-0okdbotm@yopmail.com" }

        userRepository.saveAll(listOf(custom1, custom2, tester, prof1, prof2))
    }

    fun initJobs() {

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val job1 = JobBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Plomería"]!!)
        val job2 = JobBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Albañilería"]!!)
        val job3 = JobBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Electricidad"]!!, done = false)
        val job4 = JobBuilder.buildMock(users["Valentino"]!!, users["Pablo"]!!, professions["Mecánica"]!!)

        val job5 = JobBuilder.buildMock(users["Tomas"]!!, users["Pablo"]!!, professions["Albañilería"]!!)
        val job6 = JobBuilder.buildMock(users["Tomas"]!!, users["Mariano"]!!, professions["Gasfitería"]!!)
        val job7 = JobBuilder.buildMock(users["Tomas"]!!, users["Mariano"]!!, professions["Plomería"]!!)
        val job8 = JobBuilder.buildMock(users["Tomas"]!!, users["Pablo"]!!, professions["Jardinería"]!!)

        jobRepository.saveAll(listOf(job1, job2, job3, job4, job5, job6, job7, job8))
    }

    fun initRatings() {

        val users = userRepository.findAll().associateBy { it.name }
        val jobs = jobRepository.findAll().associateBy { it.id }

        val rating1 = RatingBuilder.buildMockDTO(jobs[1]!!.id, 3)
        val rating2 = RatingBuilder.buildMockDTO(jobs[1]!!.id, 4)
        val rating3 = RatingBuilder.buildMockDTO(jobs[2]!!.id, 5)
        val rating4 = RatingBuilder.buildMockDTO(jobs[3]!!.id, 1)
        val rating5 = RatingBuilder.buildMockDTO(jobs[4]!!.id, 2)

        ratingService.rateUser(raterUserId = users["Valentino"]!!.id, rating1)
        ratingService.rateUser(raterUserId = users["Mariano"]!!.id, rating2)
        ratingService.rateUser(raterUserId = users["Valentino"]!!.id, rating3)
        ratingService.rateUser(raterUserId = users["Mariano"]!!.id, rating4)
        ratingService.rateUser(raterUserId = users["Valentino"]!!.id, rating5)
        ratingService.rateUser(raterUserId = users["Pablo"]!!.id, rating5)

    }

    fun loadJobRequestsToRedis() {

        redisService.cleanupJobRequestsForTesting()

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val jobRequest1 = JobRequestBuilder.buildMock(users["Valentino"]!!, professions["Electricidad"]!!, isInstantRequest = true)
        val jobRequest2 = JobRequestBuilder.buildMock(users["Valentino"]!!, professions["Mecánica"]!!)
        val jobRequest3 = JobRequestBuilder.buildMock(users["Valentino"]!!, professions["Albañilería"]!!)
        val jobRequest4 = JobRequestBuilder.buildMock(users["Valentino"]!!, professions["Plomería"]!!)
        val jobRequest5 = JobRequestBuilder.buildMock(users["Valentino"]!!, professions["Carpintería"]!!)
        val jobRequest6 = JobRequestBuilder.buildMock(users["Tomas"]!!, professions["Jardinería"]!!, isInstantRequest = true)
        val jobRequest7 = JobRequestBuilder.buildMock(users["Tomas"]!!, professions["Pintorería"]!!)
        val jobRequest8 = JobRequestBuilder.buildMock(users["Tomas"]!!, professions["Gasfitería"]!!)
        val jobRequest9 = JobRequestBuilder.buildMock(users["Tomas"]!!, professions["Plomería"]!!)
        val jobRequest10 = JobRequestBuilder.buildMock(users["Tomas"]!!, professions["Mecánica"]!!)

        jobService.requestJob(users["Valentino"]!!.id,jobRequest1)
        jobService.requestJob(users["Valentino"]!!.id,jobRequest2)
        jobService.requestJob(users["Valentino"]!!.id, jobRequest3)
        jobService.requestJob(users["Valentino"]!!.id, jobRequest4)
        jobService.requestJob(users["Valentino"]!!.id, jobRequest5)
        jobService.requestJob(users["Tomas"]!!.id, jobRequest6)
        jobService.requestJob(users["Tomas"]!!.id,jobRequest7)
        jobService.requestJob(users["Tomas"]!!.id,jobRequest8)
        jobService.requestJob(users["Tomas"]!!.id,jobRequest9)
        jobService.requestJob(users["Tomas"]!!.id, jobRequest10)

    }

    fun loadJobOffersToRedis() {

        redisService.cleanupJobOffersForTesting()

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val jobOffer1 = JobOfferBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Electricidad"]!!, 1)
        val jobOffer2 = JobOfferBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Mecánica"]!!, 2)
        val jobOffer3 = JobOfferBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Albañilería"]!!, 3)
        val jobOffer4 = JobOfferBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Plomería"]!!, 4)
        val jobOffer5 = JobOfferBuilder.buildMock(users["Valentino"]!!, users["Mariano"]!!, professions["Carpintería"]!!, 5)
        val jobOffer6 = JobOfferBuilder.buildMock(users["Tomas"]!!, users["Mariano"]!!, professions["Jardinería"]!!, 6)
        val jobOffer7 = JobOfferBuilder.buildMock(users["Tomas"]!!, users["Mariano"]!!, professions["Pintorería"]!!, 7)
        val jobOffer8 = JobOfferBuilder.buildMock(users["Tomas"]!!, users["Mariano"]!!, professions["Gasfitería"]!!, 8)
        val jobOffer9 = JobOfferBuilder.buildMock(users["Tomas"]!!, users["Mariano"]!!, professions["Plomería"]!!, 9)
        val jobOffer10 = JobOfferBuilder.buildMock(users["Tomas"]!!, users["Pablo"]!!, professions["Mecánica"]!!, 1)

        jobService.offerJob(users["Mariano"]!!.id, jobOffer1)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer2)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer3)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer4)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer5)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer6)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer7)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer8)
        jobService.offerJob(users["Mariano"]!!.id, jobOffer9)
        jobService.offerJob(users["Pablo"]!!.id, jobOffer10)

    }

}
