package quickfix.models

import jakarta.persistence.*
import quickfix.utils.exceptions.ProfessionalException

@Entity
@Table(name = "professional_professions")
class ProfessionalProfession : Identifier{

    @Id
    @GeneratedValue
    override var id: Long = 0

    @ManyToOne(cascade = [CascadeType.MERGE])
    lateinit var profession: Profession

    var active: Boolean = true

    fun disable() {
        if (!active) throw ProfessionalException("La profesión ya estaba deshabilitada.")
        this.active = false
    }

    fun enable() {
        if (active) throw ProfessionalException("La profesión ya estaba habilitada.")
        this.active = true
    }

    override fun validate() {
        if (profession.id <= 0) {
            throw ProfessionalException("La profesión debe existir previamente (ID inválido).")
        }
        if (profession.name.isBlank()) {
            throw ProfessionalException("El nombre de la profesión no puede estar vacío.")
        }
    }

}