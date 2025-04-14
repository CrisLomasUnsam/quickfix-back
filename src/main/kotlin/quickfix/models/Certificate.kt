package quickfix.models

import jakarta.persistence.*

@Entity
class Certificate : Identifier {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    override var id: Long = -1

    @ManyToOne
    lateinit var profession: Profession

    @ElementCollection
    var imgs : MutableSet<String> = mutableSetOf()

    override fun validate() {
        TODO("Not yet implemented")
    }
}