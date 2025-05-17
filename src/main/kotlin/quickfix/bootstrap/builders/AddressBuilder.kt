package quickfix.bootstrap.builders

import quickfix.models.Address

class AddressBuilder {
    companion object {
        fun buildMock(streetName : String) : Address {
            return Address().apply {
                street = streetName
                optional = "PB 1ºA"
                city = "Buenos Aires"
                state = "CABA"
                zipCode = "9999"
            }
        }
    }
}