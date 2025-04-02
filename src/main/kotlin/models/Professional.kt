package models

import exceptions.BusinessException
import java.time.LocalDate
import java.time.Period

class Professional(
    override var id: Int,
    override var mail: String,
    override var name: String,
    override var lastName: String,
    override var password: String,
    override var dni: Int,
    override var avatar: String,
    override var dateBirth: LocalDate,
    var professions: Set<Profession>,
    var certificates: MutableMap<Profession, List<String>>,
    var balance: Double,
    var debt: Double,
) : User(){

    private fun addConfirmedJob(): Boolean = TODO("Implement me")
    private fun removeConfirmedJob(): Boolean = TODO("Implement me")

    override fun validate() {
        if (mail.trim().isBlank()) throw BusinessException("Email cannot be empty")
        if (name.trim().isBlank()) throw BusinessException("Name cannot be empty")
        if (lastName.trim().isBlank()) throw BusinessException("Last name cannot be empty")
        if (password.trim().isBlank()) throw BusinessException("Password cannot be empty")
        if (!this.validDNI()) throw BusinessException("Invalid DNI")
        if (avatar.trim().isBlank()) throw BusinessException("Avatar cannot be empty")
        if(!this.isAdult(dateBirth)) throw BusinessException("Must be 18 or older")
    }

    private fun validDNI(): Boolean {
        val dniToString = dni.toString()
        return ( dniToString.length == 8 || dniToString.length == 7 ) && dniToString.all { it.isDigit() }
    }

    private fun isAdult(dateBirth: LocalDate): Boolean = Period.between(dateBirth, LocalDate.now()).years >= 18
}