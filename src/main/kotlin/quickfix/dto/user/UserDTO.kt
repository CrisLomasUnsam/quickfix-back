package quickfix.dto.user

import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.UserInfo
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
        fun toDTO(info: UserInfo): UserDTO {
            return UserDTO(
                mail = info.mail,
                name = info.name,
                lastName = info.lastName,
                dni = info.dni,
                avatar = info.avatar,
                dateBirth = info.dateBirth,
                gender = info.gender,
                address = info.address
            )
        }
    }
}