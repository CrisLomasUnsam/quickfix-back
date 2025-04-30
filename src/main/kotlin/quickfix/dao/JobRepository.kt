package quickfix.dao

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.models.Rating

@Component
interface JobRepository : CrudRepository<Job, Long> {

    fun findAllByCustomerId(customerId: Long): List<Job>

    fun findAllByProfessionalId(professionalId: Long): List<Job>

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
        select r.* from ratings r
        join jobs j on j.id = r.job_id
        where r.user_to_id = :userToId and j.professional_id = :userToId
    """, nativeQuery = true)
    fun findRatingsByProfessionalId(@Param("userToId") userToId: Long): List<Rating>

}