package quickfix.dto.address

import io.swagger.v3.oas.annotations.media.Schema
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.models.Address
import quickfix.models.User

@Schema(description = "DTO para dirección")
data class AddressDTO(
    var alias: String?,
    var streetAddress: String?,
    var streetReference: String?,
    var zipCode: String?,
    var state: String?,
    var city: String?,
) {
    companion object {

        fun toDTO(address: Address): AddressDTO {
            return AddressDTO(
                alias = address.alias,
                streetAddress = address.streetAddress,
                streetReference = address.streetReference,
                zipCode = address.zipCode,
                state = address.state,
                city = address.city
            )
        }

        fun fromUserModifiedInfoDTO(modifiedInfo : UserModifiedInfoDTO) : AddressDTO =
            AddressDTO(
                alias = null, //Only primary Address can be edited
                streetAddress = modifiedInfo.streetAddress,
                streetReference = modifiedInfo.streetReference,
                zipCode = modifiedInfo.zipCode,
                state = modifiedInfo.state,
                city = modifiedInfo.city,
            )
    }
}

fun AddressDTO.toAddress(user: User) : Address {
    val address = Address()
    address.alias = this.alias?.trim() ?: ""
    address.streetAddress = this.streetAddress?.trim() ?: ""
    address.streetReference = this.streetReference?.trim() ?: ""
    address.zipCode = this.zipCode?.trim() ?: ""
    address.state = this.state?.trim() ?: ""
    address.city = this.city?.trim() ?: ""
    address.user = user
    return address.apply { validate() }
}
