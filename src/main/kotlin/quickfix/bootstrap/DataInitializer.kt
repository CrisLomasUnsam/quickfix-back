package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.JobRepository
import quickfix.dao.ProfessionRepository
import quickfix.dao.RatingRepository
import quickfix.dao.UserRepository
import quickfix.models.*
import quickfix.services.ProfessionService
import quickfix.utils.ProfessionsUtils
import java.time.LocalDate

@Service
class DataInitializer : InitializingBean {

    @Autowired
    private lateinit var professionService: ProfessionService

    @Autowired
    private lateinit var ratingRepository: RatingRepository

    @Autowired
    private lateinit var jobRepository: JobRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var professionRepository: ProfessionRepository

    private lateinit var electricista: Profession
    private lateinit var gasista: Profession
    private lateinit var jardinero: Profession

    private lateinit var certificateElectricista1: Certificate
    private lateinit var certificateGasista2: Certificate
    private lateinit var certificateGasista: Certificate
    private lateinit var certificateJardinero: Certificate
    private lateinit var certificateJardinero2: Certificate

    private lateinit var professionalInfo1: ProfessionalInfo
    private lateinit var professionalInfo2: ProfessionalInfo
    private lateinit var professionalInfo3: ProfessionalInfo

    private lateinit var professional1: User
    private lateinit var professional2: User
    private lateinit var professional3: User
    private lateinit var customer1: User
    private lateinit var customer2: User
    private lateinit var customer3: User

    private lateinit var job1: Job
    private lateinit var job2: Job
    private lateinit var job3: Job

    private lateinit var rating1: Rating
    private lateinit var rating2: Rating
    private lateinit var rating3: Rating

    override fun afterPropertiesSet() {
        println("************** Initializing QuickFix Data **************")
        loadProfessions()
        initProfessions()
        initCertificates()
        initProfessionalInfos()

        if (userRepository.count() == 0L) {
            initUsers()
            initJobs()
            initRatings()
        }
        println("************** Data Initialization Complete **************")
    }

    fun loadProfessions() {
        if (professionRepository.count() == 0L) {
            val professions = ProfessionsUtils.defaultProfessions.map {
                Profession().apply { name = it }
            }
            professionRepository.saveAll(professions)
            println("Professions loaded")
        }
    }

    fun initProfessions() {
        electricista = professionService.getProfessionByName("electricista")
        gasista = professionService.getProfessionByName("gasista")
        jardinero = professionService.getProfessionByName("jardinero")
    }

    fun initCertificates() {
        certificateElectricista1 = Certificate().apply { profession = electricista }
        certificateGasista2 = Certificate().apply { profession = gasista }
        certificateGasista = Certificate().apply { profession = gasista }
        certificateJardinero = Certificate().apply { profession = jardinero }
        certificateJardinero2 = Certificate().apply { profession = jardinero }
    }

    fun initProfessionalInfos() {
        professionalInfo1 = ProfessionalInfo().apply {
            professions = mutableSetOf(electricista, gasista)
            certificates = mutableSetOf(certificateElectricista1, certificateGasista)
            balance = 0.0
            debt = 200.0
        }
        professionalInfo2 = ProfessionalInfo().apply {
            professions = mutableSetOf(jardinero)
            certificates = mutableSetOf(certificateJardinero)
            balance = 500.0
            debt = 50.0
        }
        professionalInfo3 = ProfessionalInfo().apply {
            professions = mutableSetOf(jardinero, gasista)
            certificates = mutableSetOf(certificateJardinero2, certificateGasista2)
            balance = 750.0
            debt = 100.0
        }
    }

    private fun createUser(user: User) {
        val exists = userRepository.findByDni(user.dni)
        if (exists.isPresent) {
            user.id = exists.get().id
        } else {
            userRepository.save(user)
        }
    }

    fun initUsers() {
        professional1 = User().apply {
            mail = "valen@example.com"
            name = "Valentina"
            lastName = "Gomez"
            password = "securepassword"
            dni = 12345678
            avatar = "img1"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply { street = "Calle Falsa 120"; city = "Buenos Aires"; zipCode = "1000" }
            verified = true
            professionalInfo = professionalInfo1
        }
        createUser(professional1)

        professional2 = User().apply {
            mail = "cris@example.com"
            name = "Cristina"
            lastName = "Palacios"
            password = "456123"
            dni = 12345679
            avatar = "img2"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply { street = "Calle Falsa 121"; city = "Buenos Aires"; zipCode = "1001" }
            verified = false
            professionalInfo = professionalInfo2
        }
        createUser(professional2)

        professional3 = User().apply {
            mail = "tomi@example.com"
            name = "Tomaso"
            lastName = "Perez"
            password = "pass123"
            dni = 12345671
            avatar = "img3"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply { street = "Calle Falsa 122"; city = "Buenos Aires"; zipCode = "1002" }
            verified = true
            professionalInfo = professionalInfo3
        }
        createUser(professional3)

        customer1 = User().apply {
            mail = "customer1@example.com"
            name = "Juan"
            lastName = "Contardo"
            password = "securepassword"
            dni = 12345672
            avatar = "img4"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.MALE
            address = Address().apply { street = "Calle Falsa 123"; city = "Buenos Aires"; zipCode = "1003" }
            verified = false
        }
        createUser(customer1)

        customer2 = User().apply {
            mail = "customer2@example.com"
            name = "Rodrigo"
            lastName = "Bueno"
            password = "123111"
            dni = 12345673
            avatar = "img5"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.MALE
            address = Address().apply { street = "Calle Falsa 124"; city = "Buenos Aires"; zipCode = "1004" }
            verified = false
        }
        createUser(customer2)

        customer3 = User().apply {
            mail = "customer3@example.com"
            name = "Fer"
            lastName = "Dodino"
            password = "pass123"
            dni = 12345674
            avatar = "img6"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply { street = "Calle Falsa 125"; city = "Buenos Aires"; zipCode = "1005" }
            verified = true
        }
        createUser(customer3)
    }

    fun initJobs() {
        if (jobRepository.count() == 0L) {

            val users = userRepository.findAll().associateBy { it.mail}

            job1 = Job().apply { professional = users["valen@example.com"]!!; customer = users["customer1@example.com"]!!; date = LocalDate.now(); done = true; price = 10000.0 }
            job2 = Job().apply { professional = users["cris@example.com"]!!; customer = users["customer2@example.com"]!!; date = LocalDate.now().minusDays(1); done = true; price = 20000.0 }
            job3 = Job().apply { professional = users["tomi@example.com"]!!; customer = users["customer3@example.com"]!!; date = LocalDate.now().minusDays(2); done = true; price = 30000.0 }
            jobRepository.saveAll(setOf(job1, job2, job3))
        }
    }

    fun initRatings() {
        if (ratingRepository.count() == 0L) {
            val users = userRepository.findAll().associateBy { it.mail}
            val jobs = jobRepository.findAll().associateBy { it.professional.mail }

            rating1 = Rating().apply { userFrom = users["customer1@example.com"]!!; userTo = users["valen@example.com"]!!; job = jobs["valen@example.com"]!!; score = 3; yearAndMonth = LocalDate.now() ; comment = "Muy bueno" }
            rating2 = Rating().apply { userFrom = users["customer2@example.com"]!!; userTo = users["cris@example.com"]!!; job =  jobs["cris@example.com"]!!; score = 1; yearAndMonth = LocalDate.now() ; comment = "Regular" }
            rating3 = Rating().apply { userFrom = users["customer3@example.com"]!!; userTo = users["tomi@example.com"]!!; job =  jobs["tomi@example.com"]!!; score = 5; yearAndMonth = LocalDate.now() ; comment = "Excelente" }
            ratingRepository.saveAll(setOf(rating1, rating2, rating3))
        }
    }
}
