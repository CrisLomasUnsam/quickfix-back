package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Rating
import quickfix.models.RatingStatsProjection

@Component
interface RatingRepository : JpaRepository<Rating, Long> {

//

    fun findAllByUserToId(userId: Long, pageable: Pageable): Page<Rating>

    fun findAllByUserFromId(userId: Long): List<Rating>

    fun existsByJobIdAndUserFromId(jobId: Long, userFromId: Long): Boolean

    @Query(
        value = """
        SELECT count(*) as total, 
          COALESCE(AVG(r.score), 0) as avg, 
          COALESCE(SUM(CASE WHEN r.score = 1 THEN 1 END), 0) AS amount_rating1,
          COALESCE(SUM(CASE WHEN r.score = 2 THEN 1 END), 0) AS amount_rating2,
          COALESCE(SUM(CASE WHEN r.score = 3 THEN 1 END), 0) AS amount_rating3,
          COALESCE(SUM(CASE WHEN r.score = 4 THEN 1 END), 0) AS amount_rating4,
          COALESCE(SUM(CASE WHEN r.score = 5 THEN 1 END), 0) AS amount_rating5
        FROM ratings r 
        WHERE r.user_to_id = :userId
        """,
        nativeQuery = true
    )
    fun findRatingStatsForUser(@Param("userId") userId: Long):  RatingStatsProjection
}