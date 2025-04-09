package quickfix.bootstrap

import quickfix.dao.ProfessionalRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.CustomerRepository
import quickfix.dao.UserRepository
import quickfix.models.*
import java.time.LocalDate

@Service
class DataInitializer : InitializingBean {

  @Autowired
  private lateinit var userInfoRepository: UserRepository

  @Autowired
  private lateinit var professionalRepository: ProfessionalRepository

  @Autowired
  private lateinit var customerRepository: CustomerRepository


  //***********************
  //USER
  //***********************

  val valenInfo = User().apply {
    mail = "valen@example.com"
    name = "Valentina"
    lastName = "Gomez"
    password = "securepassword"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address("Calle Falsa 123", "Buenos Aires", "1000")
    verified = true
    professional = valen
  }

  val crisInfo = User().apply {
    mail = "cris@example.com"
    name = "Cristina"
    lastName = "Palacios"
    password = "456123"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address("Calle 123", "Buenos Aires", "1000")
    verified = false
    professional = crisL
  }

  val tomiInfo = User().apply {
    mail = "tomi@example.com"
    name = "Tomaso"
    lastName = "Perez"
    password = "pass123"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address("Calle Segura 444", "Buenos Aires", "1000")
    verified = true
    professional = tomi

  }

  val juanInfo = User().apply {
    mail = "juan@example.com"
    name = "Juan"
    lastName = "Contardo"
    password = "securepassword"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.MALE
    address = Address("Calle Falsa 123", "Formosa", "444")
    verified = false
    customer = juan
  }

  val rodriInfo = User().apply {
    mail = "rodri@example.com"
    name = "Rodrigo"
    lastName = "Bueno"
    password = "123111"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.MALE
    address = Address("Calle 123", "Buenos Aires", "1000")
    verified = false
    customer = rodri
  }

  val ferInfo = User().apply {
    mail = "fer@example.com"
    name = "Fer"
    lastName = "Dodino"
    password = "pass123"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address("Calle Segura 444", "Buenos Aires", "1000")
    verified = true
    customer = fer
  }

  //***********************
  //PROFESSIONALS
  //***********************

  val valen = Professional().apply{
    balance = 0.0
    professions = mutableSetOf(Profession.ELECTRICISTA, Profession.GASISTA)
    certificates = mutableMapOf(
      Profession.ELECTRICISTA to listOf("Certificado de electricista"),
      Profession.GASISTA to listOf("gasista Matriculado")
    )
    debt = 200.0
  }

  val crisL = Professional().apply{
    professions = mutableSetOf(Profession.JARDINERO)
    certificates = mutableMapOf(Profession.JARDINERO to listOf("Certificado en jardinero profesional"))
    balance = 500.0
    debt = 50.0
  }

  val tomi = Professional().apply{
    professions = mutableSetOf(Profession.JARDINERO, Profession.GASISTA)
    certificates = mutableMapOf(
      Profession.JARDINERO to listOf("certificado en jardineria profesional"),
      Profession.GASISTA to listOf("gasista matriculado"))
    balance = 750.0
    debt = 100.0
  }

  //***********************
  //CUSTOMERS
  //***********************

  val juan = Customer()
  val rodri = Customer()
  val fer = Customer()


  fun createUserInfo() {
    userInfoRepository.apply {
      create(valenInfo)
      create(crisInfo)
      create(tomiInfo)
      create(juanInfo)
      create(rodriInfo)
      create(ferInfo)
    }
  }


  fun createProfessional() {
    professionalRepository.apply {
      create(valen)
      create(crisL)
      create(tomi)
    }
    println("Profesionales agregados")
  }


  fun createCustomer() {
    customerRepository.apply {
      create(juan)
      create(rodri)
      create(fer)
    }
    println("Customers agregados")
  }

  override fun afterPropertiesSet() {
    this.createProfessional()
    this.createCustomer()
    this.createUserInfo()
  }

}


