package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.dto.rating.IRatingInfo
import quickfix.models.Rating
import java.util.*

@Component
interface RatingRepository : JpaRepository<Rating, Long> {

    fun findAllByUserToId(userId: Long, pageable: Pageable): Page<Rating>

    fun findAllByUserFromId(userId: Long): List<Rating>

    fun existsByJobIdAndUserFromId(jobId: Long, userFromId: Long): Boolean

    fun findByJobId(jobId: Long): Optional<Rating>

    @Query(value = """
      select count(j.professional_id) as amount, 
       coalesce(sum(r.score), 0) as total
        from ratings r
        join jobs j on r.job_id = j.id
        where j.professional_id = :professionalId;
    """, nativeQuery = true)
    fun findProfessionalRatingInfo(
        @Param("professionalId") professionalId: Long): IRatingInfo

    @Query(value = """
      select count(j.customer_id) as amount, 
       coalesce(sum(r.score), 0) as total
        from ratings r
        join jobs j on r.job_id = j.id
        where j.customer_id = :customerId;
    """, nativeQuery = true)
    fun findCustomerRatingInfo(
        @Param("customerId") customerId: Long): IRatingInfo

    @Modifying
    @Query("update users set average_rating = :average where id = :userId", nativeQuery = true)
    fun setAverageRatingForCustomer(@Param("userId") userId: Long, @Param("average") average: Double)

    @Modifying
    @Query("update professionals p set average_rating = :average from users u where u.professional_info_id = p.id and u.id = :userId", nativeQuery = true)
    fun setAverageRatingForProfessional(@Param("userId") userId: Long, @Param("average") average: Double)

}