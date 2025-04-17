package quickfix.dto.user

import quickfix.models.Address
import quickfix.models.Gender

data class UserModifiedInfoDTO(
    var mail: String?,
    var name: String?,
    var lastName: String?,
    var dni: Int?,
    var dateBirth: String?,
    var gender: Gender?,
    var address: Address?
)
