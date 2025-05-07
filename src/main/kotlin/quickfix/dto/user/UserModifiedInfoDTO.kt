package quickfix.dto.user

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.dto.address.AddressDTO

@Schema(description = "actualiza datos del User")
data class UserModifiedInfoDTO(
    var mail: String?,
    var name: String?,
    var lastName: String?,
    var dateBirth: String?,
    var gender: String?,
    var address: AddressDTO?
)