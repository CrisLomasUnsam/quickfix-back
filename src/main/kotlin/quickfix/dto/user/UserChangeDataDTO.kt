data class UserChangeDataDTO(
    var mail: String,
    var name: String,
    var lastName: String,
    var dni: Int,
    var dateBirth: LocalDate,
    var gender: Gender,
    var address: Address
)