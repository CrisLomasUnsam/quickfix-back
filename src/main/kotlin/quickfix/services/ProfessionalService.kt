package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dto.job.JobSearchRequestDTO

@Service
class ProfessionalService(

)  {
    fun jobRequest(jobSearchRequestDTO: JobSearchRequestDTO) {


        // Este service mete ese jobRequest en una base no relacional, como redis o DB
    }
}