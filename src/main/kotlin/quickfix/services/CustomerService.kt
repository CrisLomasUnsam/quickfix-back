package quickfix.services

import org.springframework.stereotype.Component
import quickfix.dao.JobRepository
import quickfix.models.Job
import quickfix.utils.JobSearchParameters

@Component
class CustomerService(
    private val jobRepository: JobRepository
){

    fun getJobsByParameter(id: Long, parameter: String): List<Job> {
        val searchParameters = JobSearchParameters(parameter)
        return jobRepository.searchByParameters(id, searchParameters)
    }
}

