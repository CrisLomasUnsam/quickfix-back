package models

import java.time.LocalDate

enum class Profession {
    JARDINERO,
    GASISTA,
    ELECTRICISTA
}

class Professional(
    override var id: Int,
    override var mail: String,
    override var name: String,
    override var lastName: String,
    override var password: String,
    override var dni: Int,
    override var avatar: String,
    override var dateBirth: LocalDate,
    var professions: MutableSet<Profession>,
    var certificates: MutableMap<Profession, List<String>>,
    var balace: Double,
    var debt: Double,
) : User(){

    private fun addConfirmedJob(): Boolean = TODO("Implement me")
    private fun removeConfirmedJob(): Boolean = TODO("Implement me")
}