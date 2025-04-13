package quickfix.models

import jakarta.persistence.*

@Entity
class ProfessionalInfo : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @ManyToMany(cascade = [(CascadeType.ALL)])
    var professions: Set<Profession> = mutableSetOf()

    @OneToMany(cascade = [(CascadeType.ALL)])
    var certificates: MutableSet<Certificate> = mutableSetOf()

    var balance: Double = 0.0
    var debt: Double = 0.0

    override fun validate() {
    }

    private fun addConfirmedJob(): Boolean = TODO("Implement me")
    private fun removeConfirmedJob(): Boolean = TODO("Implement me")

}