package quickfix.services

import org.springframework.stereotype.Component
import quickfix.dao.JobRepository
import quickfix.utils.JobSearchParameters

@Component
class CustomerService(
    private val jobRepository: JobRepository
){

    fun getJobsByParameters(id: Long, parameters: JobSearchParameters) =
        jobRepository.searchByParameters(id, parameters)
}