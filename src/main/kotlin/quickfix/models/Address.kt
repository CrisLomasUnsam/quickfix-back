package quickfix.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import quickfix.dto.address.AddressDTO
import quickfix.utils.exceptions.BusinessException

@Entity
@Table(name = "addresses")
class Address : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    lateinit var street: String
    lateinit var city: String
    lateinit var zipCode: String

    fun updateAddressInfo(modifiedAddressDTO: AddressDTO) {
        modifiedAddressDTO.street?.takeIf { it.isNotBlank() }?.let {
            this.street = it
        }
        modifiedAddressDTO.city?.takeIf { it.isNotBlank() }?.let {
            this.city = it
        }
        modifiedAddressDTO.zipCode?.takeIf { it.isNotBlank() }?.let {
            this.zipCode = it
        }

    }

    override fun validate() {
        if (!validName(street)) throw BusinessException("La calle debe contener sólo letras")
        if (!validName(city)) throw BusinessException("La ciudad debe contener sólo letras")
        if (!validZipCode()) throw BusinessException("El código postal deben contener sólo números")
    }

    private fun validZipCode(): Boolean =
        zipCode.isNotBlank() && zipCode.all { it.isDigit() } && (zipCode.length in 4..5)

    private fun validName(name: String): Boolean = name.isNotBlank() && name.all { it.isLetter()}
}
