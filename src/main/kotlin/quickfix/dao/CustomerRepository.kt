package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Customer
import quickfix.models.Professional

@Component
class CustomerRepository: Repository<Customer>() {

    fun findByMail(mail: String): Customer? =
        this.elements.find { it.info.mail == mail }

}