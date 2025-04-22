package quickfix.models

import jakarta.persistence.*

@Entity
class Certificate : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    lateinit var name: String

    @ManyToOne
    lateinit var profession: Profession

    lateinit var img : String

    override fun validate() {
        TODO("Not yet implemented")
    }
}