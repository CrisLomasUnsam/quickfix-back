package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.CustomerRepository
import quickfix.dao.ProfessionalRepository

@Service
class UserService (
    val professionalRepository: ProfessionalRepository,
    val customerRepository: CustomerRepository
) {

}