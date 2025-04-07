package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.utils.SearchParameters

@Component
class JobRepository(customerRepository: CustomerRepository) : Repository<Job>(

){

    fun setToDone(id: Long){
        TODO("Not yet implemented")
    }

    fun setToCancelled(id: Long){
        TODO("Not yet implemented")
    }

    override fun searchByParameters(id: Long, parameters: SearchParameters<Job>): List<Job> {
        val jobsFilteredByCustomer = this.elements.filter { it.customer.id == id }
        return jobsFilteredByCustomer.filter { parameters.matches(it) }
    }
}