package quickfix.bootstrap.builders
import quickfix.models.Address
import quickfix.models.Profession
import quickfix.models.User

class ProfessionalBuilder {

    companion object {
        fun buildMock(userName: String, professions: Iterable<Profession>): User {
            val professional = CustomerBuilder.buildMock(userName)
            professional.professionalInfo.apply {
                balance = 100999.0
                debt = 100.0
                professions.map{addProfession(it)}
                hasVehicle = true
            }
            return professional
        }
    }
}