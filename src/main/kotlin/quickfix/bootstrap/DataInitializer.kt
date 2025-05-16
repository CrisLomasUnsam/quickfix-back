package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.bootstrap.builders.*
import quickfix.dao.*

@Service
class DataInitializer : InitializingBean {

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
        val tester = CustomerBuilder.buildMock("tester").apply { this.mail = "alt.gm-0okdbotm@yopmail.com" }

        userRepository.saveAll(listOf(custom1, custom2, custom3, prof1, prof2, prof3, tester))
    }

    fun initJobs() {

        val users = userRepository.findAll().associateBy { it.name }
        val professions = professionRepository.findAll().associateBy { it.name }

        val job1 = JobBuilder.buildMock(users["custom1"]!!, users["prof1"]!!, professions["Plomería"]!!, done = true)
        val job2 = JobBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, professions["Albañilería"]!!, done = true)
        val job3 = JobBuilder.buildMock(users["custom1"]!!, users["prof2"]!!, professions["Electricidad"]!!, done = false)
        jobRepository.saveAll(listOf(job1, job2, job3))
    }
}
