package models

import exceptions.BusinessException
import java.time.LocalDate

abstract class User : Id {
    abstract var mail: String
    abstract var name : String
    abstract var lastName : String
    abstract var password : String
    abstract var dni : Int
    abstract var avatar: String
    abstract var dateBirth : LocalDate
    //var gender : Gender
    //var address : Address

    override fun validate() {
        this.validateCommonFields()
    }

    private fun validateCommonFields() {
        if (mail.isBlank() || !mail.contains("@"))
            throw BusinessException("El mail no es válido")
        if (name.isBlank())
            throw BusinessException("El nombre no puede estar vacío")
        if (lastName.isBlank())
            throw BusinessException("El apellido no puede estar vacío")
        if (password.length < 6)
            throw BusinessException("La contraseña debe tener al menos 6 caracteres")
        if (dni.toString().length != 8)
            throw BusinessException("El DNI debe tener 8 dígitos")
        if (!dateBirth.isBefore(LocalDate.now()))
            throw BusinessException("La fecha de nacimiento debe ser válida")
        if (!dateBirth.plusYears(18).isBefore(LocalDate.now()))
            throw BusinessException("El usuario debe ser mayor a 18 años")
    }

}