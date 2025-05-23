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


    @Query("select r.* from ratings r inner join jobs j on r.job_id = j.id where j.customer_id = :customerId and r.user_to_id = :customerId", nativeQuery = true)
    fun findAllByCustomerToId(@Param("customerId") customerId: Long, pageable: Pageable): Page<Rating>

    @Query("select r.* from ratings r inner join jobs j on r.job_id = j.id where j.customer_id = :customerId and r.user_to_id = :customerId and r.score = :score", nativeQuery = true)
    fun findByCustomerToIdAndScore(@Param("customerId") customerId: Long, @Param("score") score : Int, pageable: Pageable): Page<Rating>

    @Query("select r.* from ratings r inner join jobs j on r.job_id = j.id where j.professional_id = :professionalId and r.user_to_id = :professionalId", nativeQuery = true)
    fun findAllByProfessionalToId(@Param("professionalId") professionalId: Long, pageable: Pageable): Page<Rating>

    @Query("select r.* from ratings r inner join jobs j on r.job_id = j.id where j.professional_id = :professionalId and r.user_to_id = :professionalId and r.score = :score", nativeQuery = true)
    fun findByProfessionalToIdAndScore(@Param("professionalId") professionalId: Long, @Param("score") score : Int, pageable: Pageable): Page<Rating>

    @Query("select r.* from ratings r inner join jobs j on r.job_id = j.id where j.customer_id = :customerId and r.user_from_id = :customerId and j.id = :jobId", nativeQuery = true)
    fun findByCustomerFromIdAndJobId(@Param("customerId") customerId: Long, @Param("jobId") jobId: Long): Rating?

    @Query("select r.* from ratings r inner join jobs j on r.job_id = j.id where j.professional_id = :professionalId and r.user_from_id = :professionalId and j.id = :jobId", nativeQuery = true)
    fun findByProfessionalFromIdAndJobId(@Param("professionalId") professionalId : Long, @Param("jobId") jobId: Long): Rating?

    fun existsByJobIdAndUserFromId(jobId: Long, userFromId: Long): Boolean

    @Query("select r.* from ratings r inner join jobs j on r.job_id = j.id where j.id = :jobId and r.user_from_id = :userFromId", nativeQuery = true)
    fun findByJobIdAndUserFromId(@Param("jobId") jobId: Long,@Param("userFromId") userFromId: Long): Optional<Rating>

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