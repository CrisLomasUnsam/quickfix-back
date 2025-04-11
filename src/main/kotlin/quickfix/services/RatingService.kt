package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.JobRepository
import quickfix.dao.UserRepository
import quickfix.dto.rating.RatingDTO
import quickfix.models.Rating

@Service
class RatingService(
    val userRepository: UserRepository,
    val jobRepository: JobRepository,
) {
    fun rateUser(ratingDTO: RatingDTO) {
        val userFrom = userRepository.getById(ratingDTO.userFromId)
        val userTo = userRepository.getById(ratingDTO.userToId)
        val job = jobRepository.getById(ratingDTO.jobId)

        val rating = Rating().apply {
            //Acá se asignan las variables
        }
    }
}