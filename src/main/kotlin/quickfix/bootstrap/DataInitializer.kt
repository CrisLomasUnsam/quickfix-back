package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.*
import quickfix.models.*
import quickfix.utils.ProfessionsUtils
import java.time.LocalDate
import java.time.YearMonth

@Service
class DataInitializer : InitializingBean {

    @Autowired
    private lateinit var professionalInfoRepository: ProfessionalInfoRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

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

    private lateinit var address1: Address
    private lateinit var address2: Address
    private lateinit var address3: Address
    private lateinit var address4: Address
    private lateinit var address5: Address
    private lateinit var address6: Address

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
        initAddresses()
        initUsers()
        initJobs()
        initRatings()
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
        electricista = professionRepository.findByNameContainingIgnoreCase("electricista")
        gasista = professionRepository.findByNameContainingIgnoreCase("gasista")
        jardinero = professionRepository.findByNameContainingIgnoreCase("jardinero")
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
            balance = 0.0
            professions = mutableSetOf(electricista, gasista)
            certificates = mutableSetOf(certificateElectricista1, certificateGasista)
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

//        if (professionalInfoRepository.count() == 0L) {
//            professionalInfoRepository.save(professionalInfo1)
//            professionalInfoRepository.save(professionalInfo2)
//            professionalInfoRepository.save(professionalInfo3)
//        }
    }

    fun initAddresses() {
        //address1 = Address().apply { street = "Calle Falsa 120"; city = "Buenos Aires"; zipCode = "1000" }
        //address2 = Address().apply { street = "Calle Falsa 121"; city = "Buenos Aires"; zipCode = "1001" }
        //address3 = Address().apply { street = "Calle Falsa 122"; city = "Buenos Aires"; zipCode = "1002" }
       // address4 = Address().apply { street = "Calle Falsa 123"; city = "Buenos Aires"; zipCode = "1003" }
        //address5 = Address().apply { street = "Calle Falsa 124"; city = "Buenos Aires"; zipCode = "1004" }
        //address6 = Address().apply { street = "Calle Falsa 125"; city = "Buenos Aires"; zipCode = "1005" }

//        if (addressRepository.count() == 0L) {
//            listOf(address1, address2, address3, address4, address5, address6).forEach {
//                addressRepository.save(it)
//            }
//        }
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
        professional2 = User().apply {
            mail = "cris@example.com"
            name = "Cristina"
            lastName = "Palacios"
            password = "456123"
            dni = 12345677
            avatar = "img2"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply { street = "Calle Falsa 121"; city = "Buenos Aires"; zipCode = "1001" }
            verified = false
            professionalInfo = professionalInfo2
        }
        professional3 = User().apply {
            mail = "tomi@example.com"
            name = "Tomaso"
            lastName = "Perez"
            password = "pass123"
            dni = 12345675
            avatar = "img3"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply { street = "Calle Falsa 122"; city = "Buenos Aires"; zipCode = "1002" }
            verified = true
            professionalInfo = professionalInfo3
        }
        customer1 = User().apply {
            mail = "juan@example.com"
            name = "Juan"
            lastName = "Contardo"
            password = "securepassword"
            dni = 12345673
            avatar = "img4"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.MALE
            address = Address().apply { street = "Calle Falsa 123"; city = "Buenos Aires"; zipCode = "1003" }
            verified = false
        }
        customer2 = User().apply {
            mail = "rodri@example.com"
            name = "Rodrigo"
            lastName = "Bueno"
            password = "123111"
            dni = 12345672
            avatar = "img5"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.MALE
            address = Address().apply { street = "Calle Falsa 124"; city = "Buenos Aires"; zipCode = "1004" }
            verified = false
        }
        customer3 = User().apply {
            mail = "fer@example.com"
            name = "Fer"
            lastName = "Dodino"
            password = "pass123"
            dni = 12345678
            avatar = "img6"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = Address().apply { street = "Calle Falsa 125"; city = "Buenos Aires"; zipCode = "1005" }
            verified = true
        }

        userRepository.saveAll(listOf(professional1, professional2, professional3, customer1, customer2, customer3))
    }

    fun initJobs() {
        job1 = Job().apply { professional = professional1; customer = customer1; date = LocalDate.now(); done = true; price = 10000.0 }
        job2 = Job().apply { professional = professional2; customer = customer2; date = LocalDate.now().minusDays(1); done = true; price = 20000.0 }
        job3 = Job().apply { professional = professional3; customer = customer3; date = LocalDate.now().minusDays(2); done = true; price = 30000.0 }

        if (jobRepository.count() == 0L) {
            jobRepository.saveAll(listOf(job1, job2, job3))
        }
    }

    fun initRatings() {
        rating1 = Rating().apply { userFrom = customer1; userTo = professional1; job = job1; score = 3; yearAndMonth = YearMonth.now() ; comment = "Muy bueno" }
        rating2 = Rating().apply { userFrom = customer2; userTo = professional2; job = job2; score = 1; yearAndMonth = YearMonth.now() ; comment = "Regular" }
        rating3 = Rating().apply { userFrom = customer3; userTo = professional3; job = job3; score = 5; yearAndMonth = YearMonth.now() ; comment = "Excelente" }

        if (ratingRepository.count() == 0L) {
            ratingRepository.saveAll(listOf(rating1, rating2, rating3))
        }
    }
}
