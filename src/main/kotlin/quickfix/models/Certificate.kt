package quickfix.models

import jakarta.persistence.*

@Entity
class Certificate : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var profession: Profession

    @ElementCollection
    var imgs : MutableSet<String> = mutableSetOf()

    override fun validate() {
        TODO("Not yet implemented")
    }
}