package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.ProfessionRepository
import quickfix.dao.UserRepository
import quickfix.models.*
import quickfix.utils.ProfessionsUtils
import java.time.LocalDate

@Service
class DataInitializer : InitializingBean {

  @Autowired
  private lateinit var userRepository: UserRepository

  @Autowired
  private lateinit var professionRepository: ProfessionRepository

  /* * * * * * * * * * * *
  *      USERS           *
  * * * * * * * * * * * */

  val professional1 = User().apply {
    mail = "valen@example.com"
    name = "Valentina"
    lastName = "Gomez"
    password = "securepassword"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address().apply {
        street = "Calle Falsa 123"
        city= "Buenos Aires"
        zipCode = "1000"
    }
    verified = true
  }

  val professional2 = User().apply {
    mail = "cris@example.com"
    name = "Cristina"
    lastName = "Palacios"
    password = "456123"
    dni = 12345677
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address().apply {
        street = "Calle Falsa 123"
        city= "Buenos Aires"
        zipCode = "1000"
      }
    verified = false
  }

  val professional3 = User().apply {
    mail = "tomi@example.com"
    name = "Tomaso"
    lastName = "Perez"
    password = "pass123"
    dni = 12345675
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address().apply {
        street = "Calle Segura 444"
        city = "Buenos Aires"
        zipCode = "1000"
    }
    verified = true
  }

  val user4 = User().apply {
    mail = "juan@example.com"
    name = "Juan"
    lastName = "Contardo"
    password = "securepassword"
    dni = 12345673
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.MALE
    address = Address().apply {
        street = "Calle Falsa 123"
        city = "Formosa"
        zipCode = "444"
    }
    verified = false
  }

  val user5 = User().apply {
    mail = "rodri@example.com"
    name = "Rodrigo"
    lastName = "Bueno"
    password = "123111"
    dni = 12345672
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.MALE
    address = Address().apply {
        street = "Calle 123"
        city = "Buenos Aires"
        zipCode= "1000"
    }
    verified = false
  }

  val user6 = User().apply {
    mail = "fer@example.com"
    name = "Fer"
    lastName = "Dodino"
    password = "pass123"
    dni = 12345678
    avatar = "https://static.wikia.nocookie.net/universo-reynandez/images/d/d7/Henry_Cavill.jpg/revision/latest?cb=20221023034502&path-prefix=es"
    dateBirth = LocalDate.of(1995, 5, 23)
    gender = Gender.FEMALE
    address = Address().apply {
        street = "Calle Segura 444"
        city = "Buenos Aires"
        zipCode = "1000"
    }
    verified = true
  }


  private fun createUsers() {
    userRepository.apply {
      save(professional1)
      save(professional2)
      save(professional3)
      save(user4)
      save(user5)
      save(user6)
    }
    println("------- USERS CREATED & LOADED ------- ")
  }

    /* * * * * * * * * * * *
    *      PROFESSIONALS   *
    * * * * * * * * * * * */

  private fun addProfessionalInfo() {
    val electricista = professionRepository.findByNameContainingIgnoreCase("electricista")
    val gasista = professionRepository.findByNameContainingIgnoreCase("gasista")
    val jardinero = professionRepository.findByNameContainingIgnoreCase("jardinero")

    professional1.professionalInfo.apply{
      balance = 0.0
      professions = mutableSetOf(electricista,gasista)
      certificates = mutableSetOf(
//          Certificate().apply {
//              this.profession = electricista },
//          Certificate().apply {
//              this.profession = gasista
//          }
      )
      debt = 200.0
    }

    professional2.professionalInfo.apply{
      professions = mutableSetOf(jardinero)
      certificates = mutableSetOf(
          Certificate().apply {
//              this.profession = jardinero
          }
      )
      balance = 500.0
      debt = 50.0
    }

    professional3.professionalInfo.apply{
      professions = mutableSetOf(jardinero,gasista)
      certificates = mutableSetOf(
//          Certificate().apply {
//              this.profession = jardinero
//          },
//          Certificate().apply {
//              this.profession = gasista
//          }
      )
      balance = 750.0
      debt = 100.0
    }
    println("------- PROFESSIONS ASSIGNED ----------------- ")
    println("------- PROFESSIONALS CREATED & LOADED ------- ")
  }

    /* * * * * * * * * * * *
    *      PROFESSIONS     *
    * * * * * * * * * * * */

    private fun loadProfessions() {
        if (professionRepository.count() == 0L) {
            val professions = ProfessionsUtils.defaultProfessions
                .map { Profession().apply { name = it } }
            professionRepository.saveAll(professions)

            println("------- PROFESSIONS LOADED ------- ")
        }
    }

    private fun printSucess() {
        println("* * * * * * * * * * * * * * * * * * * * * * *")
        println("* ------- PROFESSIONS LOADED -------------- *")
        println("* ------- PROFESSIONS ASSIGNED ------------ *")
        println("* ------- PROFESSIONALS CREATED & LOADED -- *")
        println("* ------- USERS CREATED & LOADED ---------- *")
        println("* * * * * * * * * * * * * * * * * * * * * * *")
    }

  override fun afterPropertiesSet() {
    this.loadProfessions()
    this.addProfessionalInfo()
    this.createUsers()
    this.printSucess()
  }
}