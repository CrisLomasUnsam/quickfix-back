package quickfix.dto.user

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "actualiza datos del User")
data class UserModifiedInfoDTO(
    var mail: String?,
    var name: String?,
    var lastName: String?,
    var dni: Int?,
    var dateBirth: String?,
    var gender: String?,
    var streetAddress: String?,
    var streetReference: String?,
    var zipCode: String?,
    var city: String?,
    var state: String?,
)