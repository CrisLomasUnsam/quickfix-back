package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.JobRepository
import quickfix.dto.job.JobUpdateDTO
import quickfix.models.Job

@Service
class JobService(
    val jobRepository: JobRepository
){
    fun createJob(job: Job) = jobRepository.create(job)

    fun deleteJob(job: Job) = jobRepository.delete(job)

    fun getJobById(id: Int): Job? = jobRepository.getById(id)

    fun setJobAsDone(id: Int) = jobRepository.setToDone(id)

    fun setJobAsCancelled(id: Int) = jobRepository.setToCancelled(id)
}