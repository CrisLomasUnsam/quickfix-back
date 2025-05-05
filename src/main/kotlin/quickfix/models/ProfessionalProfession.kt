package quickfix.models

import jakarta.persistence.*

@Entity
@Table(name = "professional_professions")
class ProfessionalProfession : Identifier{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long = -1

    @ManyToOne(cascade = [CascadeType.ALL])
    lateinit var profession: Profession

    var active: Boolean = true

    fun disable() {
        this.active = false
    }

    fun enable() {
        this.active = true
    }

    override fun validate() {}

}