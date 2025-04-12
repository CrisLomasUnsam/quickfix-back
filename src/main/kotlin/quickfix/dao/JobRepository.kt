package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.utils.searchParameters.ISearchParameters
import quickfix.utils.exceptions.BusinessException

@Component
interface JobRepository : CrudRepository<Job, Long> {

    fun setToDone(id: Long){
        TODO("Not yet implemented")
    }

    fun setToCancelled(id: Long){
        TODO("Not yet implemented")
    }

    fun searchByParameters(id: Long, parameters: ISearchParameters<Job>): List<Job> {
        val jobsFilteredByCustomer = this.findAll().filter { it.customer.id == id }
        return jobsFilteredByCustomer.filter { parameters.matches(it) }
    }

    fun getAllByUserId(customerId: Long): List<Job> =
        this.findAll()
            .filter { it.customer.id == customerId }
            .ifEmpty { throw BusinessException("No existen servicios pertenecientes al cliente.") }
}