package quickfix.bootstrap.builders

import quickfix.models.Address
import quickfix.models.User

class AddressBuilder {
    companion object {
        fun buildPrimaryMocks(user: User, streetName : String) : Address {
            return Address().apply {
                this.user = user
                this.alias = "Principal"
                this.principal = true
                this.streetAddress = streetName
                this.streetReference = "PB 1ºA"
                this.city = "Belgrano"
                this.state = "Ciudad Autónoma de Buenos Aires"
                this.zipCode = "9999"
            }
        }
    }
}