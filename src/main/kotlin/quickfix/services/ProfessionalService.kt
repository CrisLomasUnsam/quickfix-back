package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dto.job.JobRequestDTO

@Service
class ProfessionalService(
    //Va a tener inyectado el servicio de redis, como mínimo
)  {
    fun lookForJobRequests() : Set<JobRequestDTO>? {
        /* Este metodo busca nuevas solicitudes */
        return null
    }
}