package quickfix.dto.register

import quickfix.models.Address
import quickfix.models.Customer
import quickfix.models.Gender
import quickfix.models.Professional
import java.time.LocalDate

data class RegisterRequestDTO(
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

fun RegisterRequestDTO.toCustomer() : Customer =
    Customer(
        mail = this.mail.trim(),
        name = this.name.trim(),
        lastName = this.lastName.trim(),
        password = this.password.trim(),
        dni = this.dni,
        avatar = this.avatar.trim(),
        dateBirth = this.dateBirth,
        gender = this.gender,
        address = this.address
    ).apply {
        validate()
    }


fun RegisterRequestDTO.toProfessional() : Professional =
    Professional(
        mail = this.mail.trim(),
        name = this.name.trim(),
        lastName = this.lastName.trim(),
        password = this.password.trim(),
        dni = this.dni,
        avatar = this.avatar.trim(),
        dateBirth = this.dateBirth,
        balance = 0.0,
        debt = 0.0,
        certificates = mutableMapOf(),
        professions = mutableSetOf(),
        gender = this.gender,
        address = this.address
    ).apply {
        validate()
    }