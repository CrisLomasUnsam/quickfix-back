package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository
import quickfix.models.User
import quickfix.utils.exceptions.BusinessException

@Service
class UserService (
    val professionalRepository: ProfessionalRepository,
    val customerRepository: CustomerRepository
) {
    fun findById(id: Long): User  {
        val customer = customerRepository.getById(id)
        if (customer != null) return customer

        val professional = professionalRepository.getById(id)
        if (professional != null) return professional

        throw BusinessException("There is no element associated with the id:: $id")
    }
}