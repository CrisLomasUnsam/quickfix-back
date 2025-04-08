package mocks

import quickfix.models.*
import java.time.LocalDate

data class CustomerMock(
    val customer: UserInfo
)

fun createCustomerMock(): CustomerMock {
    val mockCustomer = UserInfo().apply {
        mail = "test.user@example.com"
        name = "Test"
        lastName = "User"
        password = "securePassword123"
        dni = 12345678
        avatar = "https://example.com/avatar.jpg"
        dateBirth = LocalDate.of(1995, 5, 20)
        gender = Gender.MALE
        address = Address(
            street = "123 Main Street",
            city = "Testville",
            zipCode = "1234"
        )
    }
    return CustomerMock(mockCustomer)
}