package quickfix.models
import jakarta.persistence.*
import quickfix.utils.exceptions.BusinessException

@Entity
@Table(name = "certificates")
class Certificate : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    lateinit var name: String

    @ManyToOne
    lateinit var profession: Profession

    lateinit var img : String

    override fun validate() {
        if (!validName()) throw BusinessException("El nombre del certificado no puede estar vacío")
        if (!validImg()) throw BusinessException("El formato de la imagen no es válido")
        validProfession()
    }

    private fun validProfession() = profession.validate()

    private fun validImg(): Boolean {
        val imageRegex = Regex(""".*\.(jpg|jpeg|png|svg)$""", RegexOption.IGNORE_CASE)
        return img.isNotBlank() && imageRegex.matches(img)
    }

    private fun validName(): Boolean = name.isNotBlank() && name.all { it.isLetter() }

}