package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.JobRepository
import quickfix.models.Job
import quickfix.utils.searchParameters.JobSearchParameters

@Service
class JobService(
    val jobRepository: JobRepository
){
    fun createJob(job: Job) = jobRepository.save(job)

    fun deleteJob(job: Job) = jobRepository.delete(job)

    fun getJobById(id: Long): Job? = jobRepository.findById(id).orElseThrow()

    fun getJobsByUser(id: Long) = jobRepository.getAllByUserId(id)

    fun setJobAsDone(id: Long) = jobRepository.setToDone(id)

    fun setJobAsCancelled(id: Long) = jobRepository.setToCancelled(id)

    fun getJobsByParameter(id: Long, parameter: String): List<Job> {
        val searchParameters = JobSearchParameters(parameter)
        return jobRepository.searchByParameters(id, searchParameters)
    }
}