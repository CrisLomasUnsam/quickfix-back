package quickfix.dto.user

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
        fun toDTO(user : User) : UserProfileInfoDto{
            return UserProfileInfoDto(
                mail = user.mail,
                name = user.name,
                lastName = user.lastName,
                dni = user.dni,
                dateBirth = stringifyDate(user.dateBirth, DateWithDayFormatter),
                gender = user.gender,
                streetAddress1 = user.address.street,
                streetAddress2 = user.address.optional,
                zipCode = user.address.zipCode,
                city = user.address.city,
                state = user.address.state,
                avatar = getAvatarUrl(user.id),
            )
        }
    }
}