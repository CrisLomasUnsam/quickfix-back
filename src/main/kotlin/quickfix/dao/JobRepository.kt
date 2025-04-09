package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.utils.ISearchParameters
import quickfix.utils.exceptions.BusinessException

@Component
class JobRepository : Repository<Job>(

){

    fun setToDone(id: Long){
        TODO("Not yet implemented")
    }

    fun setToCancelled(id: Long){
        TODO("Not yet implemented")
    }

    override fun searchByParameters(id: Long, parameters: ISearchParameters<Job>): List<Job> {
        val jobsFilteredByCustomer = this.elements.filter { it.customer.id == id }
        return jobsFilteredByCustomer.filter { parameters.matches(it) }
    }

    fun getAllByCustomerId(customerId: Long): List<Job> =
        elements.filter { it.customer.id == customerId }.ifEmpty { throw BusinessException("No existen servicios pertenecientes al cliente.") }
}