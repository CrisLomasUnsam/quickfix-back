package quickfix.bootstrap

import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import quickfix.dao.AddressRepository
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

  @Autowired
  private lateinit var addressRepository: AddressRepository


  /* * * * * * * * * * * *
   *      ADDRESS        *
   * * * * * * * * * * * */


  val address1 = Address().apply {
    street = "Rafaela 5053"
    city = "CABA"
    zipCode = "A3052"
  }

  val address2 = Address().apply {
    street = "Av. Corrientes 3247"
    city = "CABA"
    zipCode = "C1193"
  }

  val address3 = Address().apply {
    street = "San Martín 850"
    city = "San Miguel de Tucumán"
    zipCode = "T4000"
  }

  val address4 = Address().apply {
    street = "Boulevard Oroño 1265"
    city = "Rosario"
    zipCode = "S2000"
  }

  val address5 = Address().apply {
    street = "Av. Colón 1654"
    city = "Córdoba"
    zipCode = "X5000"
  }



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
    address = address1
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
    address = address2
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
    address = address3
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
    address = address4
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
    address = address5
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
    address = address1
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

    val certificateElectricista1 = Certificate().apply { this.profession = electricista }
    val certificateGasista2 = Certificate().apply { this.profession = gasista }
    val certificateGasista = Certificate().apply { this.profession = gasista }
    val certificateJardinero = Certificate().apply { this.profession = jardinero }
    val certificateJardinero2 = Certificate().apply { this.profession = jardinero }

      professional1.professionalInfo.apply{
      balance = 0.0
      professions = mutableSetOf(electricista,gasista)
      certificates = mutableSetOf(
        certificateElectricista1,
        certificateGasista,
      )
      debt = 200.0
    }

    professional2.professionalInfo.apply{
      professions = mutableSetOf(jardinero)
      certificates = mutableSetOf(
          certificateJardinero
      )
      balance = 500.0
      debt = 50.0
    }

    professional3.professionalInfo.apply{
      professions = mutableSetOf(jardinero,gasista)
      certificates = mutableSetOf(
        certificateJardinero2,
        certificateGasista2,
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

  /* * * * * * * * * * * *
   *      ADDRESS        *
   * * * * * * * * * * * */

  private fun createAdress() {
    addressRepository.apply {
      save(address1)
      save(address2)
      save(address3)
      save(address4)
      save(address5)
    }
    println("------- ADDRESS CREATED & LOADED ------- ")

  }

    private fun printSucess() {
        println("* * * * * * * * * * * * * * * * * * * * * * *")
        println("* ------- PROFESSIONS LOADED -------------- *")
        println("* ------- PROFESSIONS ASSIGNED ------------ *")
        println("* ------- PROFESSIONALS CREATED & LOADED -- *")
        println("* ------- ADDRESS CREATED & LOADED -------- *")
        println("* ------- USERS CREATED & LOADED ---------- *")
        println("* * * * * * * * * * * * * * * * * * * * * * *")
    }

  override fun afterPropertiesSet() {
    this.loadProfessions()
    this.addProfessionalInfo()
    this.createUsers()
    this.printSucess()
    this.createAdress()
  }
}