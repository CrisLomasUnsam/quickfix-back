package quickfix.dto.register

import quickfix.models.*
import quickfix.utils.DateWithDayFormatter
import quickfix.utils.datifyString
import quickfix.utils.stringifyDate

class RegisterRequestDTO (
    var mail: String,
    var name: String,
    var lastName: String,
    var password: String,
    var dni: Int,
    var avatar: String,
    var dateBirth: String,
    var gender: Gender,
    var address: Address,
){
    companion object {
        fun toDTO(user: User): RegisterRequestDTO {
            return RegisterRequestDTO(
                mail = user.mail,
                name = user.name,
                lastName = user.lastName,
                password = user.password,
                dni = user.dni,
                avatar = user.avatar,
                dateBirth = stringifyDate(user.dateBirth, DateWithDayFormatter),
                gender = user.gender,
                address = user.address
            )
        }
    }
}

fun RegisterRequestDTO.toUser() : User {
    val request : RegisterRequestDTO = this
    return User().apply {
        mail = request.mail.trim()
        name = request.name.trim()
        lastName = request.lastName.trim()
        password = request.password.trim()
        dni = request.dni
        avatar = request.avatar.trim()
        dateBirth = datifyString(request.dateBirth, DateWithDayFormatter)
        gender = request.gender
        address = request.address
    }.apply {
        validate()
    }
}