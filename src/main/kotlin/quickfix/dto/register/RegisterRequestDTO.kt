package quickfix.dto.register

import quickfix.dto.address.AddressDTO
import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.User
import quickfix.utils.datifyStringWithDay

class RegisterRequestDTO (
    var mail: String,
    var name: String,
    var lastName: String,
    var rawPassword: String,
    var dni: Int,
    var dateBirth: String,
    var gender: Gender,
    var address: AddressDTO,
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
        address = Address().apply {
            street = request.address.street!!
            optional = request.address.optional!!
            zipCode = request.address.zipCode!!
            state = request.address.state!!
            city = request.address.city!!
        }
        setNewPassword(request.rawPassword.trim())
    }.apply {
        validate()
    }
}