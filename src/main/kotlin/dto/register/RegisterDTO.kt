package dto.register

import models.*
import java.time.LocalDate

data class CustomerRegisterRequestDTO(
    var mail: String,
    var name: String,
    var lastName: String,
    var password: String,
    var dni: Int,
    var avatar: String,
    var dateBirth: LocalDate,
    var gender: Gender,
    var age: Int,
    var address: Address
)

fun CustomerRegisterRequestDTO.fromDTO(): Customer {
    return Customer(
        id = 0,
        mail = this.mail.trim(),
        name = this.name.trim(),
        lastName = this.lastName.trim(),
        password = this.password.trim(),
        dni = this.dni,
        avatar = this.avatar.trim(),
        dateBirth = this.dateBirth,
    ).apply {
        validate()
    }
}

data class ProfessionalRegisterRequestDTO(
    var mail: String,
    var name: String,
    var lastName: String,
    var password: String,
    var dni: Int,
    var avatar: String,
    var dateBirth: LocalDate,
    var gender: Gender,
    var age: Int,
    var address: Address,
    var professions: Set<Profession>,
    var certificates: MutableMap<Profession,List<String>>,
    var balance: Double,
    var debt: Double
)

fun ProfessionalRegisterRequestDTO.fromDTO(): Professional {
    return Professional(
        id = 0,
        mail = this.mail.trim(),
        name = this.name.trim(),
        lastName = this.lastName.trim(),
        password = this.password.trim(),
        dni = this.dni,
        avatar = this.avatar.trim(),
        dateBirth = this.dateBirth,
        balance = this.balance,
        debt = this.debt,
        certificates = certificates,
        professions = professions
    ).apply {
        validate()
    }
}
