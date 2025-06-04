package quickfix.dto.register

import quickfix.models.Gender
import quickfix.models.User
import quickfix.utils.functions.datifyStringWithDay

class RegisterRequestDTO (
    var mail: String,
    var name: String,
    var lastName: String,
    var rawPassword: String,
    var dni: Int,
    var dateBirth: String,
    var gender: Gender,
    var streetAddress1: String,
    var streetAddress2: String?,
    var zipCode: String,
    var city: String,
    var state: String,

)

fun RegisterRequestDTO.toUser() : User {
    val request : RegisterRequestDTO = this

    return User().apply {
        mail = request.mail.trim()
        name = request.name.trim()
        lastName = request.lastName.trim()
        dni = request.dni
        dateBirth = datifyStringWithDay(request.dateBirth)
        gender = request.gender
        setNewPassword(request.rawPassword.trim())
    }.apply {
        validate()
    }
}