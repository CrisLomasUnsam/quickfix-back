package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Customer

@Component
class CustomerRepository: Repository<Customer>() {

}