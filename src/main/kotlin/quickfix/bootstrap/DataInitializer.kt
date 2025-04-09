package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.UserRepository
import quickfix.models.*
import java.time.LocalDate

@Service
class DataInitializer : InitializingBean {

  @Autowired
  private lateinit var userRepository: UserRepository

  //***********************
  //USERS
  //***********************

  val valen = User().apply {
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
  }

  val cris = User().apply {
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
  }

  val tomi = User().apply {
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
  }

  val juan = User().apply {
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
  }

  val rodri = User().apply {
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
  }

  val fer = User().apply {
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
  }


  fun createUsers() {
    userRepository.apply {
      create(valen)
      create(cris)
      create(tomi)
      create(juan)
      create(rodri)
      create(fer)
    }
  }


  fun addProfessions() {
    //***********************
    //PROFESSIONALS
    //***********************

    valen.professional.apply{
      balance = 0.0
      professions = mutableSetOf(Profession.ELECTRICISTA, Profession.GASISTA)
      certificates = mutableMapOf(
        Profession.ELECTRICISTA to listOf("Certificado de electricista"),
        Profession.GASISTA to listOf("gasista Matriculado")
      )
      debt = 200.0
    }

    cris.professional.apply{
      professions = mutableSetOf(Profession.JARDINERO)
      certificates = mutableMapOf(Profession.JARDINERO to listOf("Certificado en jardinero profesional"))
      balance = 500.0
      debt = 50.0
    }

    tomi.professional.apply{
      professions = mutableSetOf(Profession.JARDINERO, Profession.GASISTA)
      certificates = mutableMapOf(
        Profession.JARDINERO to listOf("certificado en jardineria profesional"),
        Profession.GASISTA to listOf("gasista matriculado"))
      balance = 750.0
      debt = 100.0
    }
    println("Profesionales agregados")
  }


  override fun afterPropertiesSet() {
    this.addProfessions()
    this.createUsers()
  }

}


