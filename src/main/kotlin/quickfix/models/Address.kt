package quickfix.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Address : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    lateinit var street: String
    lateinit var city: String
    lateinit var zipCode: String

    override fun validate() {
        TODO("Not yet implemented")
    }
}