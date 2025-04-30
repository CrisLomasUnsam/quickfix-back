package quickfix.mock

import quickfix.models.Address
import quickfix.models.Gender
import quickfix.models.User
import java.time.LocalDate

data class CustomerMock(
    val customerUser: User
)

fun createCustomerMock(): CustomerMock {
    val mockCustomer = User().apply {
        mail = "test.user@example.com"
        name = "Carlos"
        lastName = "Perez"
        password = "securePassword123"
        dni = 12456778
        avatar = "https://example.com/avatar.jpg"
        dateBirth = LocalDate.of(1995, 5, 20)
        gender = Gender.MALE
        address = Address().apply {
            street = "123 Main Street"
            city = "Testville"
            zipCode = "1234"
        }
        verified = true
    }
    return CustomerMock(mockCustomer)
}