package quickfix.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import quickfix.dto.address.AddressDTO

@Entity
@Table(name = "addresses")
class Address : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    lateinit var street: String
    lateinit var city: String
    lateinit var zipCode: String

    override fun validate() {
        TODO("Not yet implemented")
    }

    fun updateAddressInfo(modifiedAddressDTO: AddressDTO) {
        modifiedAddressDTO.street?.let {
            this.street = it
        }
        modifiedAddressDTO.city?.let {
            this.city = it
        }
        modifiedAddressDTO.zipCode?.let {
            this.zipCode = it
        }

    }
}