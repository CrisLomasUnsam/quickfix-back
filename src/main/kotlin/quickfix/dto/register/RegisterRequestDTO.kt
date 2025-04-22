package quickfix.dto.register

import quickfix.models.*
import quickfix.utils.DateWithDayFormatter
import quickfix.utils.datifyString

class RegisterRequestDTO (
    var mail: String,
    var name: String,
    var lastName: String,
    var rawPassword: String,
    var dni: Int,
    var avatar: String,
    var dateBirth: String,
    var gender: Gender,
    var address: Address,
)

fun RegisterRequestDTO.toUser() : User {
    val request : RegisterRequestDTO = this
    return User().apply {
        mail = request.mail.trim()
        name = request.name.trim()
        lastName = request.lastName.trim()
        password = request.rawPassword.trim()
        dni = request.dni
        avatar = request.avatar.trim()
        dateBirth = datifyString(request.dateBirth, DateWithDayFormatter)
        gender = request.gender
        address = request.address
    }.apply {
        validate()
    }
}