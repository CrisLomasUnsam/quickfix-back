package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Rating
import java.util.*

@Component
interface RatingRepository : JpaRepository<Rating, Long> {

    fun findAllByUserToId(userId: Long, pageable: Pageable): Page<Rating>

    fun findAllByUserFromId(userId: Long): List<Rating>

    fun existsByJobIdAndUserFromId(jobId: Long, userFromId: Long): Boolean

    fun findByJobIdAndUserFromId(jobId: Long, userFromId: Long): Optional<Rating>

 @Query(value = """
     SELECT COALESCE(AVG(r.score), 0) 
     FROM ratings r
    WHERE r.user_to_id = :userToId
 """,
     nativeQuery = true)
    fun findAverageRatingByUserToId(@Param("userToId") userToId: Long): Double
}