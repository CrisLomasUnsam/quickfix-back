package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.JobRepository
import quickfix.models.Job

@Service
class JobService(
    val jobRepository: JobRepository
){
    fun createJob(job: Job) = jobRepository.create(job)

    fun deleteJob(job: Job) = jobRepository.delete(job)

    fun getJobById(id: Long): Job? = jobRepository.getById(id)

    fun getJobsByCustomer(id: Long) = jobRepository.getAllByCustomerId(id)

    fun setJobAsDone(id: Long) = jobRepository.setToDone(id)

    fun setJobAsCancelled(id: Long) = jobRepository.setToCancelled(id)
}