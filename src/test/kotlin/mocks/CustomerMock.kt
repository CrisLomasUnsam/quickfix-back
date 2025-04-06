package mocks

import quickfix.models.Address
import quickfix.models.Customer
import quickfix.models.Gender
import java.time.LocalDate

data class CustomerMock(
    val customer: Customer
)

fun createCustomerMock(): CustomerMock {
    val mockCustomer = Customer(
        mail = "test.user@example.com",
        name = "Test",
        lastName = "User",
        password = "securePassword123",
        dni = 12345678,
        avatar = "https://example.com/avatar.jpg",
        dateBirth = LocalDate.of(1995, 5, 20),
        gender = Gender.MALE, // o Gender.FEMALE
        address = Address(
            street = "123 Main Street",
            city = "Testville",
            zipCode = "1234"
        )
    )
    return CustomerMock(mockCustomer)
}