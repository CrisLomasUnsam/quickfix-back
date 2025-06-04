package quickfix.dto.address

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Address
import quickfix.models.User

@Schema(description = "DTO para dirección")
data class AddressDTO(
    var alias: String?,
    var streetAddress1: String?,
    var streetAddress2: String?,
    var zipCode: String?,
    var state: String?,
    var city: String?,
) {
    companion object {

        fun toDTO(address: Address): AddressDTO {
            return AddressDTO(
                alias = address.alias,
                streetAddress1 = address.streetAddress1,
                streetAddress2 = address.streetAddress2,
                zipCode = address.zipCode,
                state = address.state,
                city = address.city
            )
        }

        fun fromUserModifiedInfoDTO(modifiedInfo : UserModifiedInfoDTO) : AddressDTO =
            AddressDTO(
                alias = null, //Only primary Address can be edited
                streetAddress1 = modifiedInfo.streetAddress1,
                streetAddress2 = modifiedInfo.streetAddress2,
                zipCode = modifiedInfo.zipCode,
                state = modifiedInfo.state,
                city = modifiedInfo.city,
            )
    }
}

fun AddressDTO.toAddress(user: User) : Address {
    val address = Address()
    address.alias = this.alias?.trim() ?: ""
    address.streetAddress1 = this.streetAddress1?.trim() ?: ""
    address.streetAddress2 = this.streetAddress2?.trim() ?: ""
    address.zipCode = this.zipCode?.trim() ?: ""
    address.state = this.state?.trim() ?: ""
    address.city = this.city?.trim() ?: ""
    address.user = user
    return address.apply { validate() }
}
