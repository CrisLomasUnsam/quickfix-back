package quickfix.models

import jakarta.persistence.*
import quickfix.dto.address.AddressDTO
import quickfix.utils.exceptions.AddressException

@Entity
@Table(name = "addresses")
class Address : Identifier {

    @Id @GeneratedValue
    override var id: Long = 0

    @ManyToOne
    lateinit var user : User

    @Column(length = 25, nullable = false)
    lateinit var alias: String

    var principal = false
    lateinit var streetAddress1: String
    var streetAddress2: String = ""
    lateinit var zipCode: String
    lateinit var state: String
    lateinit var city: String


    fun updateAddressInfo(modifiedAddressDTO: AddressDTO) {
        modifiedAddressDTO.streetAddress1?.takeIf { it.isNotBlank() }?.let {
            this.streetAddress1 = it
        }
        modifiedAddressDTO.streetAddress2?.takeIf { it.isNotBlank() }?.let {
            this.streetAddress2 = it
        }
        modifiedAddressDTO.zipCode?.takeIf { it.isNotBlank() }?.let {
            this.zipCode = it
        }
        modifiedAddressDTO.state?.takeIf { it.isNotBlank() }?.let {
            this.state = it
        }
        modifiedAddressDTO.city?.takeIf { it.isNotBlank() }?.let {
            this.city = it
        }

    }

    override fun validate() {
        if (!validName(alias.trim())) throw AddressException("Ingrese un alias que contenga solo números y letras.")
        if (!validName(streetAddress1.trim())) throw AddressException("Ingrese una dirección válida.")
        if (!validName(city.trim())) throw AddressException("Ingrese una ciudad válida.")
        if (!validName(state.trim())) throw AddressException("Ingrese una provincia válida.")
        if (!validZipCode()) throw AddressException("El código postal debe contener 4 o 5 números")
    }

    private fun validZipCode(): Boolean =
        zipCode.isNotBlank() && zipCode.all { it.isDigit() } && (zipCode.length in 4..5)

    private fun validName(name: String): Boolean = name.isNotBlank() && name.all { it.isLetter() || it.isDigit() || it == ' '}

}
