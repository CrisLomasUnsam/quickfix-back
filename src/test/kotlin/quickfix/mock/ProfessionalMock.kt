package quickfix.mock

import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.ProfessionalInfo
import quickfix.models.User
import java.time.LocalDate

data class ProfessionalMock(val professionalUser: User)

fun createProfessionalMock(): ProfessionalMock {
    val professional = User().apply {
        mail = "profesional@example.com"
        name = "Ana"
        lastName = "Gómez"
        password = "profesional123"
        dni = 42644333
        avatar = "https://example.com/avatar-profesional.jpg"
        dateBirth = LocalDate.of(1985, 6, 15)
        gender = Gender.FEMALE
        address = Address().apply {
            street = "Calle Falsa 456"
            city = "Shelbyville"
            zipCode = "5678"
        }
        verified = true
        professionalInfo = ProfessionalInfo().apply {
            balance = 1000.0
            debt = 100.0
            hasVehicle = true
        }
    }
    return ProfessionalMock(professional)
}
