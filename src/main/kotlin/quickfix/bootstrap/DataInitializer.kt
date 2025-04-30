package quickfix.bootstrap

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import quickfix.dao.*
import quickfix.models.*
import quickfix.security.Roles
import quickfix.services.AddressService
import quickfix.services.ProfessionService
import quickfix.utils.dataInitializer.Professions
import java.time.LocalDate

@Service
class DataInitializer : InitializingBean {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var roleRepository: RoleRepository

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

    val logger: Logger = LoggerFactory.getLogger(DataInitializer::class.java)

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

    private fun createRole(roleName: String): Rol {
        val rol = roleRepository.findByName(roleName)
        if (rol.isEmpty) {
            logger.info("Creando rol $roleName")
            return roleRepository.save(Rol().apply {
                name = roleName
            })
        } else {
            logger.info("Rol $roleName ya existe")
            return rol.get()
        }
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
        certificateElectricista1 = Certificate().apply { profession = electricista; name = "Capacitación de Electricista" ; img = "img1" }
        certificateGasista = Certificate().apply { profession = gasista; name = "Matrícula de Gasista" ; img = "img2" }
        certificateGasista2 = Certificate().apply { profession = gasista; name = "Matrícula de Gasista 2" ; img = "img3" }
        certificateJardinero = Certificate().apply { profession = jardinero; name = "Curso de Jardinería" ; img = "img4" }
        certificateJardinero2 = Certificate().apply { profession = jardinero; name = "Curso de Florista" ; img = "img5" }
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

    private fun createUser(user: User, rol: Rol) {
        val exists = userRepository.findByDni(user.dni)
        if (exists != null) {
            user.id = exists.id
        }
        userRepository.save(user.apply {
            this.password = passwordEncoder.encode(user.password)
            addRole(rol)
        })
    }

    fun initUsers() {

        val admin = createRole(Roles.ADMIN.name)
        val readonly = createRole(Roles.READONLY.name)
        val customer = createRole(Roles.CUSTOMER.name)
        val professional = createRole(Roles.PROFESSIONAL.name)

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

        createUser(professional1, professional)

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

        createUser(professional2, professional)

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

        createUser(professional3, professional)

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

        createUser(customer1, customer)

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

        createUser(customer2, customer)

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

        createUser(customer3, customer)
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
            rating3 = Rating().apply { userFrom = users["customer3@example.com"]!!; userTo = users["tomi@example.com"]!!; job =  jobs["tomi@example.com"]!!; score = 5; yearAndMonth = LocalDate.now() ; comment = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget suscipit tortor, at sodales sapien." }
            ratingRepository.saveAll(setOf(rating1, rating2, rating3))
        }

    }
}
