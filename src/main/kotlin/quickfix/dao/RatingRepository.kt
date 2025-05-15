package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import quickfix.models.Rating
import java.util.*

@Component
interface RatingRepository : JpaRepository<Rating, Long> {

//

    fun findAllByUserToId(userId: Long, pageable: Pageable): Page<Rating>

    fun findAllByUserFromId(userId: Long): List<Rating>

    fun existsByJobIdAndUserFromId(jobId: Long, userFromId: Long): Boolean

    fun findByJobId(jobId: Long): Optional<Rating>
}