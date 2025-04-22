package quickfix.dto.address

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO para dirección")
data class AddressDTO(
    var street: String?,
    var city: String?,
    var zipCode: String?
)

