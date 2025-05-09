package quickfix.models

import jakarta.persistence.*
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.utils.CONFIRM_FRONTEND_URL
import quickfix.utils.FRONTEND_URL
import quickfix.utils.datifyStringWithDay
import quickfix.utils.exceptions.BusinessException
import quickfix.utils.exceptions.InvalidCredentialsException
import java.time.LocalDate

@Entity
@Table(name = "users")
class User : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @Column(length = 97)
    private lateinit var password : String

    @Column(unique = true)
    var dni : Int = 0

    @Enumerated(EnumType.STRING)
    lateinit var gender : Gender

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    lateinit var address : Address

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var professionalInfo: ProfessionalInfo = ProfessionalInfo()

    lateinit var mail: String
    lateinit var name : String
    lateinit var lastName : String
    lateinit var avatar: String
    lateinit var dateBirth : LocalDate
    var verified : Boolean = false

    companion object {
        const val EDAD_REQUERIDA = 18
    }

    fun updateUserInfo(modifiedInfoDTO: UserModifiedInfoDTO) {
        modifiedInfoDTO.mail
            ?.takeIf { it.isNotBlank() }
            ?.let {
                val oldMail = this.mail
                this.mail = it
                if (!validMail()) {
                    this.mail = oldMail
                    throw BusinessException("Email inválido")
                }
            }

        modifiedInfoDTO.name
            ?.takeIf { it.isNotBlank() }
            ?.let {
                if (!validName(it)) throw BusinessException("Nombre sólo puede contener letras")
                this.name = it
            }

        modifiedInfoDTO.lastName?.takeIf { it.isNotBlank() }?.let {
            if (!validName(it)) throw BusinessException("Apellido sólo puede contener letras")
            this.lastName = it
        }

        modifiedInfoDTO.dateBirth
            ?.takeIf { it.isNotBlank() }
            ?.let {
                val oldDate = this.dateBirth
                this.dateBirth = datifyStringWithDay(modifiedInfoDTO.dateBirth!!)
                if (!isAdult()) {
                    this.dateBirth = oldDate
                    throw BusinessException("Debe ser mayor de edad")
                }
            }

        modifiedInfoDTO.gender
            ?.takeIf { it.isNotBlank() }
            ?.let { Gender.fromName(it) }
            ?.let { this.gender = it }

        modifiedInfoDTO.address
            ?.takeIf {
                it.street?.isNotBlank() == true ||
                it.optional?.isNotBlank() == true ||
                it.zipCode?.isNotBlank() == true ||
                it.state?.isNotBlank() == true ||
                it.city?.isNotBlank() == true
            }
            ?.let { addressDTO ->
                this.address.updateAddressInfo(addressDTO)
            }

    }

    private fun getDefaultEncoder(): PasswordEncoder =
        Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()!!

    fun setNewPassword(rawPassword: String) {
        validateRawPassword(rawPassword)
        password = getDefaultEncoder().encode(rawPassword)
    }

    fun verifyPassword(rawPassword: String) : Boolean =
        getDefaultEncoder().matches(rawPassword, password)

    private fun validateRawPassword(rawPassword: String) {
        val validPassword = rawPassword.trim().length >= 6 && !(rawPassword.trim().contains(" "))
        if(!validPassword)
            throw InvalidCredentialsException()
    }

    override fun validate() = validateCommonFields()

    private fun validateCommonFields() {

        if (!this.validMail())
            throw BusinessException("El email no es válido.")

        if (!this.validName(name))
            throw BusinessException("El nombre no puede estar vacío ni contener caracteres especiales o numéricos.")

        if (!this.validName(lastName))
            throw BusinessException("El apellido no puede estar vacío ni contener caracteres especiales o numéricos.")

        if (!this.validDNI())
            throw BusinessException("El DNI es incorrecto.")

        if (!this.isAdult())
            throw BusinessException("El usuario debe ser mayor a $EDAD_REQUERIDA años.")

        if (!dateBirth.isBefore(LocalDate.now()))
            throw BusinessException("La fecha de nacimiento no es válida.")
    }


    private fun validDNI(): Boolean {
        val dniToString = dni.toString()
        return ( dniToString.length == 8 || dniToString.length == 7 ) && dniToString.all { it.isDigit() }
    }

    private fun validMail() : Boolean =
        mail.trim().isNotBlank() && mail.trim().contains("@") && !mail.trim().contains(" ")

    private fun validName(name : String) : Boolean =
        name.trim().isNotBlank() && !name.trim().contains(" ") && !name.any { it.isDigit() }

    private fun isAdult(): Boolean =
        dateBirth.plusYears(EDAD_REQUERIDA.toLong()).isBefore(LocalDate.now())

}