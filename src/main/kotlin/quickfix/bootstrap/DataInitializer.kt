package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.*
import quickfix.models.*
import quickfix.services.*
import quickfix.utils.ProfessionsUtils
import java.time.LocalDate

@Service
class DataInitializer : InitializingBean {

    @Autowired
    private lateinit var addressService: AddressService

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

    @Autowired
    private lateinit var addressRepository: AddressRepository

    private lateinit var address1: Address
    private lateinit var address2: Address
    private lateinit var address3: Address
    private lateinit var address4: Address
    private lateinit var address5: Address
    private lateinit var address6: Address

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
        loadAddresses()
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

    fun loadAddresses() {
        if (addressRepository.count() == 0L) {
            address1 = Address().apply { street = "Rafaela 5053"; city = "CABA"; zipCode = "1000" }
            address2 = Address().apply { street = "Av. Corrientes 3247"; city = "CABA"; zipCode = "1001" }
            address3 = Address().apply { street = "San Martín 850" ; city = "San Miguel de Tucumán"; zipCode = "1002" }
            address4 = Address().apply { street = "Boulevard Oroño 1265"; city = "Rosario" ;zipCode = "1003" }
            address5 = Address().apply { street = "Av. Colón 1654"; city = "Córdoba"; zipCode = "1004" }
            address6 = Address().apply { street = "Av. Mondongo 1000"; city = "Córdoba"; zipCode = "1005"}
            addressRepository.saveAll(listOf(address1, address2, address3, address4, address5, address6))
        }
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
        address1 = addressService.getAddressByZipCode("1000")
        professional1 = User().apply {
            mail = "valen@example.com"
            name = "Valentina"
            lastName = "Gomez"
            password = "securepassword"
            dni = 12345678
            avatar = "img1"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = address1
            verified = true
            professionalInfo = professionalInfo1
        }
        createUser(professional1)

        address2 = addressService.getAddressByZipCode("1001")
        professional2 = User().apply {
            mail = "cris@example.com"
            name = "Cristina"
            lastName = "Palacios"
            password = "456123"
            dni = 12345679
            avatar = "img2"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = address2
            verified = false
            professionalInfo = professionalInfo2
        }
        createUser(professional2)

        address3 = addressService.getAddressByZipCode("1002")
        professional3 = User().apply {
            mail = "tomi@example.com"
            name = "Tomaso"
            lastName = "Perez"
            password = "pass123"
            dni = 12345671
            avatar = "img3"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = address3
            verified = true
            professionalInfo = professionalInfo3
        }
        createUser(professional3)

        address4 = addressService.getAddressByZipCode("1003")
        customer1 = User().apply {
            mail = "customer1@example.com"
            name = "Juan"
            lastName = "Contardo"
            password = "securepassword"
            dni = 12345672
            avatar = "img4"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.MALE
            address = address4
            verified = false
        }
        createUser(customer1)

        address5 = addressService.getAddressByZipCode("1004")
        customer2 = User().apply {
            mail = "customer2@example.com"
            name = "Rodrigo"
            lastName = "Bueno"
            password = "123111"
            dni = 12345673
            avatar = "img5"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.OTHER
            address = address5
            verified = false
        }
        createUser(customer2)

        address6 = addressService.getAddressByZipCode("1005")
        customer3 = User().apply {
            mail = "customer3@example.com"
            name = "Fer"
            lastName = "Dodino"
            password = "pass123"
            dni = 12345674
            avatar = "img6"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = address6
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