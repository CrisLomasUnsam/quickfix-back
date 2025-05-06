package quickfix.dto.register

import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.User
import quickfix.utils.datifyStringWithDay
import java.util.Base64

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
        dni = request.dni
        avatar =  Base64.getDecoder().decode(request.avatar.trim())
        dateBirth = datifyStringWithDay(request.dateBirth)
        gender = request.gender
        address = request.address
        setNewPassword(request.rawPassword.trim())
    }.apply {
        validate()
    }
}