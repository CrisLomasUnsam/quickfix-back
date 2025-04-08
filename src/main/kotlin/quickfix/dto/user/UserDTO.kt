package quickfix.dto.user

import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.User
import java.time.LocalDate

data class UserDTO(
    var mail: String,
    var name: String,
    var lastName: String,
    var dni: Int,
    var avatar: String,
    var dateBirth: LocalDate,
    var gender: Gender,
    var address: Address
) {

    companion object {
        fun toDTO(user: User): UserDTO {
            return UserDTO(
                mail = user.mail,
                name = user.name,
                lastName = user.lastName,
                dni = user.dni,
                avatar = user.avatar,
                dateBirth = user.dateBirth,
                gender = user.gender,
                address = user.address
            )
        }
    }
}