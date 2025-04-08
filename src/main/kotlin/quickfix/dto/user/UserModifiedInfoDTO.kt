package quickfix.dto.user

import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.UserInfo
import java.time.LocalDate

data class UserModifiedInfoDTO(
    var mail: String?,
    var name: String?,
    var lastName: String?,
    var dni: Int?,
    var dateBirth: LocalDate?,
    var gender: Gender?,
    var address: Address?
)
