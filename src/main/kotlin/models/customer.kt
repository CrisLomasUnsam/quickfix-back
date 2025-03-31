package models

import java.time.LocalDate

class Customer (
    override var id: Int,
    override var mail: String,
    override var name: String,
    override var lastName: String,
    override var password: String,
    override var dni: Int,
    override var avatar: String,
    override var dateBirth: LocalDate,
) : User() {

    override fun validate() {
        super.validate()
    }

}