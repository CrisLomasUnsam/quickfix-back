package quickfix.models

import jakarta.persistence.*

@Entity
class Certificate : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @ManyToOne
    lateinit var profession: Profession

    @ElementCollection(fetch = FetchType.EAGER)
    var imgs : MutableSet<String> = mutableSetOf()

    override fun validate() {
        TODO("Not yet implemented")
    }
}