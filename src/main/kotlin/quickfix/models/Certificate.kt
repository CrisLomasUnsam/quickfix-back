package quickfix.models
import jakarta.persistence.*
import quickfix.utils.exceptions.ProfessionalException

@Entity
@Table(name = "certificates")
class Certificate : Identifier {

    @Id @GeneratedValue
    override var id: Long = 0

    lateinit var name: String

    @ManyToOne
    lateinit var profession: Profession

    override fun validate() {
        if (!validName()) throw ProfessionalException("El nombre del certificado no puede estar vacío")
    }


    private fun validName(): Boolean = name.isNotBlank() && name.all { it.isLetter() }

}