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

      val info = customer.info

      return CustomerDTO(
        mail = info.mail,
        name = info.name,
        lastName = info.lastName,
        dni = info.dni,
        avatar = info.avatar,
        dateBirth = info.dateBirth
      )
    }
  }
}