package quickfix.dto.customer

import quickfix.models.Customer
import java.time.LocalDate


data class CustomerDTO(
  var mail: String,
  var name: String,
  var lastName: String,
  var dni: Int,
  var avatar: String,
  var dateBirth: LocalDate,
) {

  companion object {
    fun toDTO( customer: Customer): CustomerDTO {
      return CustomerDTO(
        mail = customer.mail,
        name = customer.name,
        lastName = customer.lastName,
        dni = customer.dni,
        avatar = customer.avatar,
        dateBirth = customer.dateBirth
      )
    }
  }
}