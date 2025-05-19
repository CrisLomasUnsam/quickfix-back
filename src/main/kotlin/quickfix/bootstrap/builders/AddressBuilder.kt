package quickfix.bootstrap.builders

import quickfix.models.Address

class AddressBuilder {
    companion object {
        fun buildMock(streetName : String) : Address {
            return Address().apply {
                street = streetName
                optional = "PB 1ºA"
                city = "Belgrano"
                state = "Ciudad Autónoma de Buenos Aires"
                zipCode = "9999"
            }
        }
    }
}