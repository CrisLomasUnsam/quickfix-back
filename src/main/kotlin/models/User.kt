package models

import java.time.LocalDate

interface User {
    var mail: String
    var name : String
    var lastName : String
    var password : String
    var dni : Int
    var avatar: String
    var dateBirth : LocalDate
    //var gender : Gender
    //var address : Address
}