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
    var address: Address
)

fun RegisterRequestDTO.toUserInfo() : UserInfo {

    val request : RegisterRequestDTO = this
    return UserInfo().apply {
        mail = request.mail.trim()
        name = request.name.trim()
        lastName = request.lastName.trim()
        password = request.password.trim()
        dni = request.dni
        avatar = request.avatar.trim()
        dateBirth = request.dateBirth
        gender = request.gender
        address = request.address
    }.apply {
        validate()
    }
}


fun RegisterRequestDTO.toCustomer() : Customer {

    val customer = Customer()
    val userInfo = this.toUserInfo()
    customer.info = userInfo
    return customer
}



fun RegisterRequestDTO.toProfessional() : Professional {

    val professional = Professional()
    val userInfo = this.toUserInfo()
    professional.info = userInfo
    return professional
}