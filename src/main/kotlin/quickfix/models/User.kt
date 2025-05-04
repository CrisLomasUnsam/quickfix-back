package quickfix.models

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.utils.datifyStringWithDay
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

@Entity
@Table(name = "users")
class User : Identifier, UserDetails {

    @Id @GeneratedValue
    override var id: Long = -1

    @Column(length = 60)
    private lateinit var _password : String

    @Column(unique = true)
    var dni : Int = 0

    @Enumerated(EnumType.STRING)
    lateinit var gender : Gender

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    lateinit var address : Address

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    var professionalInfo: ProfessionalInfo = ProfessionalInfo()

    @ManyToMany
    val roles: MutableSet<Rol> = mutableSetOf()

    lateinit var mail: String
    lateinit var name : String
    lateinit var lastName : String
    lateinit var avatar: String
    lateinit var dateBirth : LocalDate
    var verified : Boolean = false

    companion object {
        const val EDAD_REQUERIDA = 18
    }

    fun setPassword(password: String) {
        this._password = password
    }

    override fun getUsername(): String = mail

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf() /*Esto es para roles*/

    override fun getPassword(): String = _password

    fun addRole(role: Rol) {
        if (!roles.contains(role)) roles.add(role)
    }

    override fun validate() = validateCommonFields()

    private fun validateCommonFields() {

        if (!this.validMail())
            throw BusinessException("El email no es válido.")

        if (!this.validName(name))
            throw BusinessException("El nombre no puede estar vacío ni contener caracteres especiales o numéricos.")

        if (!this.validName(lastName))
            throw BusinessException("El apellido no puede estar vacío ni contener caracteres especiales o numéricos.")

        if (!this.validPassword())
            throw BusinessException("La contraseña debe tener al menos 6 caracteres y no debe contener espacios en blanco.")

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

    private fun validPassword() : Boolean =
        password.trim().length >= 6 && !(password.trim().contains(" "))

    private fun isAdult(): Boolean =
        dateBirth.plusYears(EDAD_REQUERIDA.toLong()).isBefore(LocalDate.now())

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
            ?.let { Gender.fromNombre(it) }
            ?.let { this.gender = it }

        modifiedInfoDTO.address
            ?.takeIf { it.street?.isNotBlank() == true || it.city?.isNotBlank() == true || it.zipCode?.isNotBlank() == true }
            ?.let { addressDTO ->
                this.address.updateAddressInfo(addressDTO)
            }
    }
}