package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.*
import quickfix.models.*
import quickfix.services.AddressService
import quickfix.services.ProfessionService
import quickfix.utils.dataInitializer.Professions
import java.time.LocalDate

@Service
class DataInitializer : InitializingBean {

//    @Autowired
//    private lateinit var userService: UserService
//
//    @Autowired
//    private lateinit var registerService: RegisterService

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

        if (userRepository.count() != 0L) {
            println("DIRTY REPO !!!!!!!")
            userRepository.deleteAll()
            //registerUsers()
            initUsers()
            initJobs()
            initRatings()
        } else {
            println("CLEAN REPO !!!!!!!!")
            //registerUsers()
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
            println("************** DIRECCIONES CARGADAS ****************")
        }
    }

    fun loadProfessions() {
        if (professionRepository.count() == 0L) {
            val professions : List<Profession> = Professions.map { professionName ->
                Profession().apply { this.name = professionName }
            }
            professionRepository.saveAll(professions)
            println("************** PROFESIONES CARGADAS ************")
        }
    }

    fun initProfessions() {
        electricista = professionService.getByNameIgnoreCase("Electricista")
        gasista = professionService.getByNameIgnoreCase("Gasista")
        jardinero = professionService.getByNameIgnoreCase("Jardinero")
    }

    fun initCertificates() {
        certificateElectricista1 = Certificate().apply { profession = electricista; img = "img1" }
        certificateGasista2 = Certificate().apply { profession = gasista; img = "img2" }
        certificateGasista = Certificate().apply { profession = gasista; img = "img3" }
        certificateJardinero = Certificate().apply { profession = jardinero; img = "img4" }
        certificateJardinero2 = Certificate().apply { profession = jardinero; img = "img5" }
    }

    fun initProfessionalInfos() {
        professionalInfo1 = ProfessionalInfo().apply {
            professions = mutableSetOf(electricista, gasista)
            certificates = mutableSetOf(certificateElectricista1, certificateGasista)
            balance = 0.0
            debt = 200.0
        }
        professionalInfo2 = ProfessionalInfo().apply {
            professions = mutableSetOf(electricista, jardinero)
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
        if (exists != null) {
            user.id = exists.id
        }
        userRepository.save(user)
    }

//    fun registerUsers() {
//        if (userRepository.count() != 0L) { println(" DIRTY REPO !!!!") }
//        val users = listOf(
//            RegisterRequestDTO(mail = "valen@example.com", name = "Valentina", lastName = "Gomez", rawPassword = "securepassword", dni = 12345678, avatar = "https://randomuser.me/api/portraits/women/1.jpg", dateBirth = "23/05/1999", gender = Gender.FEMALE, address = addressService.getAddressByZipCode("1000")),
//            RegisterRequestDTO(mail = "cris@example.com", name = "Cristina", lastName = "Palacios", rawPassword = "456123", dni = 12345679, avatar = "https://randomuser.me/api/portraits/women/2.jpg", dateBirth = "20/06/1998", gender = Gender.FEMALE, address = addressService.getAddressByZipCode("1001")),
//            RegisterRequestDTO(mail = "tomi@example.com", name = "Tomaso", lastName = "Perez", rawPassword = "pass123", dni = 12345671, avatar = "https://randomuser.me/api/portraits/women/3.jpg", dateBirth = "09/08/2000", gender = Gender.FEMALE, address = addressService.getAddressByZipCode("1002")),
//            RegisterRequestDTO(mail = "customer1@example.com", name = "Juan", lastName = "Contardo", rawPassword = "securepassword", dni = 12345672, avatar = "https://randomuser.me/api/portraits/men/2.jpg", dateBirth = "21/11/1997", gender = Gender.MALE, address = addressService.getAddressByZipCode("1003")),
//            RegisterRequestDTO(mail = "customer2@example.com", name = "Rodrigo", lastName = "Bueno", rawPassword = "123111", dni = 12345673, avatar = "https://randomuser.me/api/portraits/men/3.jpg", dateBirth = "18/07/1999", gender = Gender.OTHER, address = addressService.getAddressByZipCode("1004")),
//            RegisterRequestDTO(mail = "customer3@example.com", name = "Fer", lastName = "Dodino", rawPassword = "pass123", dni = 12345674, avatar = "https://randomuser.me/api/portraits/women/4.jpg", dateBirth = "12/04/1995", gender = Gender.FEMALE, address = addressService.getAddressByZipCode("1005"))
//        )
//        users.forEach { user ->
//            try {
//                registerService.registerUser(user)
//                println("* * * USUARIO NUEVO REGISTRADO: ${user.mail} * * *")
//            } catch (ex: BusinessException) {
//                println(ex.message)
//            }
//        }
//    }

    fun initUsers() {
        address1 = addressService.getAddressByZipCode("1000")
        professional1 = User().apply {
            mail = "valen@example.com"
            name = "Valentina"
            lastName = "Gomez"
            password = "dummyPassword"
            dni = 12345678
            avatar = "imgValen1"
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
            password = "dummyPassword"
            dni = 12345679
            avatar = "imgCris2"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = address2
            verified = true
            professionalInfo = professionalInfo2
        }
        createUser(professional2)

        address3 = addressService.getAddressByZipCode("1002")
        professional3 = User().apply {
            mail = "tomi@example.com"
            name = "Tomaso"
            lastName = "Perez"
            password = "dummyPassword"
            dni = 12345671
            avatar = "imgTomi3"
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
            password = "dummyPassword"
            dni = 12345672
            avatar = "imgJuan4"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.MALE
            address = address4
            verified = true
        }
        createUser(customer1)

        address5 = addressService.getAddressByZipCode("1004")
        customer2 = User().apply {
            mail = "customer2@example.com"
            name = "Rodrigo"
            lastName = "Bueno"
            password = "dummyPassword"
            dni = 12345673
            avatar = "imgRodri5"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.OTHER
            address = address5
            verified = true
        }
        createUser(customer2)

        address6 = addressService.getAddressByZipCode("1005")
        customer3 = User().apply {
            mail = "customer3@example.com"
            name = "Fer"
            lastName = "Dodino"
            password = "dummyPassword"
            dni = 12345674
            avatar = "imgFer6"
            dateBirth = LocalDate.of(1995, 5, 23)
            gender = Gender.FEMALE
            address = address6
            verified = true
        }
        createUser(customer3)
//        professional1 = userService.getUserByMail("valen@example.com").apply {
//            this.verified = true
//            this.professionalInfo = professionalInfo1
//        }
//        updateUser(professional1)
//
//        professional2 = userService.getUserByMail("cris@example.com").apply {
//            this.verified = false
//            this.professionalInfo = professionalInfo2
//        }
//        updateUser(professional2)
//
//        professional3 = userService.getUserByMail("tomi@example.com").apply {
//            this.verified = true
//            this.professionalInfo = professionalInfo3
//        }
//        updateUser(professional3)
//
//        customer1 = userService.getUserByMail("customer1@example.com").apply {
//            this.verified = false
//        }
//        updateUser(customer1)
//
//        customer2 = userService.getUserByMail("customer2@example.com").apply {
//            this.verified = false
//        }
//        updateUser(customer2)
//
//        customer3 = userService.getUserByMail("customer3@example.com").apply {
//            this.verified = true
//        }
//        updateUser(customer3)
    }

    fun initJobs() {
        if (jobRepository.count() == 0L) {

            val users = userRepository.findAll().associateBy { it.mail}

            job1 = Job().apply { professional = users["valen@example.com"]!!; customer = users["customer1@example.com"]!!; date = LocalDate.now(); done = true; price = 10000.0 ; profession = electricista }
            job2 = Job().apply { professional = users["cris@example.com"]!!; customer = users["customer2@example.com"]!!; date = LocalDate.now().minusDays(1); done = true; price = 20000.0; profession = jardinero  }
            job3 = Job().apply { professional = users["tomi@example.com"]!!; customer = users["customer3@example.com"]!!; date = LocalDate.now().minusDays(2); done = true; price = 30000.0; profession = gasista  }
            jobRepository.saveAll(setOf(job1, job2, job3))
        }
    }

    fun initRatings() {
        if (ratingRepository.count() == 0L) {
            val users = userRepository.findAll().associateBy { it.mail}
            val jobs = jobRepository.findAll().associateBy { it.professional.mail }

            rating1 = Rating().apply { userFrom = users["customer1@example.com"]!!; userTo = users["valen@example.com"]!!; job = jobs["valen@example.com"]!!; score = 3; yearAndMonth = LocalDate.now() ; comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget suscipit tortor, at sodales sapien."}
            rating2 = Rating().apply { userFrom = users["customer2@example.com"]!!; userTo = users["cris@example.com"]!!; job =  jobs["cris@example.com"]!!; score = 1; yearAndMonth = LocalDate.now() ; comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget suscipit tortor, at sodales sapien."}
            rating3 = Rating().apply { userFrom = users["customer3@example.com"]!!; userTo = users["tomi@example.com"]!!; job =  jobs["tomi@example.com"]!!; score = 5; yearAndMonth = LocalDate.now() ; comment = "\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget suscipit tortor, at sodales sapien." }
            ratingRepository.saveAll(setOf(rating1, rating2, rating3))
        }
    }
}