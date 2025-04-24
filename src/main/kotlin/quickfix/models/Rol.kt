package quickfix.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Rol : Identifier {

    @Id
    @GeneratedValue
    override var id: Long = -1
    var name: String = ""

    override fun validate() {}
}