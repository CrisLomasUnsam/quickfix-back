package models

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

    }
}