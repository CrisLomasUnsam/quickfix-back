package quickfix.bootstrap

import quickfix.dao.ProfessionalRepository
import org.springframework.beans.factory.InitializingBean
import quickfix.models.*
import java.time.LocalDate

class DataInitializer(private val professionalRepository: ProfessionalRepository) : InitializingBean {

  val valen = Professional(
    mail = "valen@example.com",
    name = "Valentina",
    lastName = "Gomez",
    password = "securepassword",
    dni = 12345678,
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es",
    dateBirth = LocalDate.of(1995, 5, 23),
    gender = Gender.FEMALE,
    balance = 0.0,
    professions = setOf(Profession.ELECTRICISTA, Profession.GASISTA),
    certificates = mutableMapOf(
      Profession.ELECTRICISTA to listOf("Certificado de electricista"),
      Profession.GASISTA to listOf("gasista Matriculado")),
    debt = 200.0,
    address = Address("Calle Falsa 123", "Buenos Aires", "1000")
  )

  val crisL = Professional(
    mail = "carlos@example.com",
    name = "Carlos",
    lastName = "Ramirez",
    password = "password123",
    dni = 23456789,
    avatar = "https://static.wikia.nocookie.net/rezero/images/d/d8/Subaru_-_Anime.png/revision/latest?cb=20161116222546&path-prefix=es",
    dateBirth = LocalDate.of(1988, 8, 15),
    gender = Gender.MALE,
    professions = setOf(Profession.JARDINERO),
    certificates = mutableMapOf(Profession.JARDINERO to listOf("Certificado en jardinero profesional")),
    balance = 500.0,
    debt = 50.0,
    address = Address("Avenida Siempre Viva 456", "Córdoba", "5000")
  )

  val tomi = Professional(
    mail = "lucia@example.com",
    name = "Lucia",
    lastName = "Fernandez",
    password = "pass456",
    dni = 34567890,
    avatar = "https://media.lmneuquen.com/p/ac4a5a4782776acb97f5537f8875dcab/adjuntos/195/imagenes/004/084/0004084082/tony-starkkkjpg.jpg",
    dateBirth = LocalDate.of(1992, 3, 10),
    gender = Gender.FEMALE,
    professions = setOf(Profession.JARDINERO, Profession.GASISTA),
    certificates = mutableMapOf(
      Profession.JARDINERO to listOf("certificado en jardineria profesional"),
      Profession.GASISTA to listOf("gasista matriculado")),
    balance = 750.0,
    debt = 100.0,
    address = Address("Calle 123", "Rosario", "2000")
  )

  val juan = Customer(
    mail = "lucia@example.com",
    name = "Lucia",
    lastName = "Fernandez",
    password = "pass456",
    dni = 34567890,
    avatar = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRC_CFS2UQUMKIgZOib9wa6ZnMxagLGSWNB6A&s",
    dateBirth = LocalDate.of(1992, 3, 10),
    gender = Gender.FEMALE,
    address = Address("Calle 123", "Rosario", "2000")
  )

  val rodi = Customer(
    mail = "lucia@example.com",
    name = "Lucia",
    lastName = "Fernandez",
    password = "pass456",
    dni = 34567890,
    avatar = "https://media.lmneuquen.com/p/ac4a5a4782776acb97f5537f8875dcab/adjuntos/195/imagenes/004/084/0004084082/tony-starkkkjpg.jpg",
    dateBirth = LocalDate.of(1992, 3, 10),
    gender = Gender.FEMALE,
    address = Address("Calle 123asd", "Rosario", "2000")
  )


  val fer = Customer(
    mail = "lucia@example.com",
    name = "Lucia",
    lastName = "Fernandez",
    password = "pass456",
    dni = 34567890,
    avatar = "https://media.lmneuquen.com/p/ac4a5a4782776acb97f5537f8875dcab/adjuntos/195/imagenes/004/084/0004084082/tony-starkkkjpg.jpg",
    dateBirth = LocalDate.of(1992, 3, 10),
    gender = Gender.FEMALE,
    address = Address("Calle 123", "Rosario", "2000")
  )

  fun createProfessional() {
    professionalRepository.apply {
      create(valen)
      create(crisL)
      create(tomi)
    }
  }


  fun createCustomer() {

  }

  override fun afterPropertiesSet() {
    this.createProfessional()
    this.createCustomer()
  }


}