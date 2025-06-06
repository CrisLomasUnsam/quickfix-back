package quickfix.bootstrap.builders
import quickfix.models.Profession
import quickfix.models.User

class ProfessionalBuilder {

    companion object {
        fun buildMock(userName: String, lastName: String = "Test", professions: Iterable<Profession> = listOf(), mail: String): User {
            val professional = CustomerBuilder.buildMock(userName, lastName)
            professional.mail = mail.lowercase()
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