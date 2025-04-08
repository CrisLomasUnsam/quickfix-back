package quickfix.dto.register

import quickfix.models.*
import java.time.LocalDate


class RegisterRequestDTO (
    var mail: String,
    var name: String,
    var lastName: String,
    var password: String,
    var dni: Int,
    var avatar: String,
    var dateBirth: LocalDate,
    var gender: Gender,
    var address: Address,
    var isProfessional: Boolean
)

fun RegisterRequestDTO.toUser() : User {

    return User().apply {
        mail = this@toUser.mail.trim()
        name = this@toUser.name.trim()
        lastName = this@toUser.lastName.trim()
        password = this@toUser.password.trim()
        dni = this@toUser.dni
        avatar = this@toUser.avatar.trim()
        dateBirth = this@toUser.dateBirth
        gender = this@toUser.gender
        address = this@toUser.address
        validate()
    }
}

