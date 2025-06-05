package quickfix.dto.user

import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.User
import quickfix.utils.functions.DateWithDayFormatter
import quickfix.utils.functions.getAvatarUrl
import quickfix.utils.functions.stringifyDate

data class UserProfileInfoDto (
    var mail: String,
    var name: String,
    var lastName: String,
    var dni: Int,
    var dateBirth: String,
    var gender: Gender,
    var streetAddress1: String,
    var streetAddress2: String?,
    var zipCode: String,
    var city: String,
    var state: String,
    val avatar: String,
)
{
    companion object{
        fun toDTO(user : User, address: Address) : UserProfileInfoDto{
            return UserProfileInfoDto(
                mail = user.mail,
                name = user.name,
                lastName = user.lastName,
                dni = user.dni,
                dateBirth = stringifyDate(user.dateBirth, DateWithDayFormatter),
                gender = user.gender,
                streetAddress1 = address.streetAddress1,
                streetAddress2 = address.streetAddress2,
                zipCode = address.zipCode,
                city = address.city,
                state = address.state,
                avatar = getAvatarUrl(user.id),
            )
        }
    }
}