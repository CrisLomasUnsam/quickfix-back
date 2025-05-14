package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.models.Rating
import java.util.Optional

@Component
interface JobRepository : JpaRepository<Job, Long> {

    fun findAllByCustomerId(customerId: Long, pageable: Pageable): Page<Job>

    fun findAllByProfessionalId(professionalId: Long, pageable: Pageable): Page<Job>

    fun existsByIdAndCustomerId(jobId: Long, customerId: Long): Boolean

    fun existsByIdAndProfessionalId(jobId: Long, professionalId: Long): Boolean

    @Query(value="""
        select professional_id from jobs where id = :jobId fetch first 1 rows only
    """, nativeQuery = true)
    fun getProfessionalIdByJobId(@Param("jobId") jobId: Long): Long

    @Query(value="""
        select customer_id from jobs where id = :jobId fetch first 1 rows only
    """, nativeQuery = true)
    fun getCustomerIdByJobId(@Param("jobId") jobId: Long): Long

    @Query(
        value = """
        select *
           from jobs j
           where j.customer_id = :customerId
            and (
                :param is null
            or j.id::text              ILIKE CONCAT('%', :param, '%')
            or j.status                ILIKE CONCAT('%', :param, '%')
            or j.profession_id::text   ILIKE CONCAT('%', :param, '%')
            or j.professional_id::text ILIKE CONCAT('%', :param, '%')
            )
        """,
        nativeQuery = true
    )
    fun findJobByFilter(@Param("customerId") customerId: Long, @Param("param") param: String?) : List<Job>

    @Query(value = """
        SELECT r.* FROM ratings r
        JOIN jobs j ON j.id = r.job_id
        WHERE r.user_to_id = :userToId AND j.customer_id = :userToId
    """, nativeQuery = true)
    fun findRatingsByCustomerId(@Param("userToId") userToId: Long): List<Rating>

    @Query(value = """
        SELECT r.score as score FROM ratings r
        JOIN jobs j ON j.id = r.job_id
        WHERE r.user_to_id = :userToId AND j.professional_id = :userToId
    """, nativeQuery = true)
    fun findRatingsByProfessionalId(@Param("userToId") userToId: Long): List<Rating>

}

