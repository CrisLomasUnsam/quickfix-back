package quickfix.models
import quickfix.utils.exceptions.BusinessException
import java.time.LocalDate

class User : Id {

    override var id: Long = -1
    lateinit var mail: String
    lateinit var name : String
    lateinit var lastName : String
    lateinit var password : String
    var dni : Int = 0
    lateinit var avatar: String
    lateinit var dateBirth : LocalDate
    lateinit var gender : Gender
    lateinit var address : Address
    var verified : Boolean = false
    var professional: Professional = Professional()
    var customer : Customer = Customer()

    companion object {
        const val EDAD_REQUERIDA = 18
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

    fun verifyPassword(password : String) : Boolean =
        this.password == password

}