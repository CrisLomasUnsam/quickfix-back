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
    override var gender: Gender,
    override var address: Address

    ) : User(){

    private fun addConfirmedJob(): Boolean = TODO("Implement me")
    private fun removeConfirmedJob(): Boolean = TODO("Implement me")

}