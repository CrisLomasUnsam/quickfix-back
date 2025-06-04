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
    var streetAddress: String,
    var streetReference: String?,
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
                streetAddress = address.streetAddress,
                streetReference = address.streetReference,
                zipCode = address.zipCode,
                city = address.city,
                state = address.state,
                avatar = getAvatarUrl(user.id),
            )
        }
    }
}