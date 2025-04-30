package quickfix.dao

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.models.Rating
import java.time.LocalDate

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

    //TODO: Test
    @Query(value= """
        select * from ratings r
        join users u on u.id = r.user_to_id
        join jobs j on j.id = r.job_id
        where r.user_to_id = :userToId and j.customer_id = :userToId
    """, nativeQuery = true)
    fun findRatingsByCustomerId(@Param("userToId") userToId: Long): List<Rating>

    //TODO: Test
    @Query(value= """
        select * from ratings r
        join users u on u.id = r.user_to_id
        join jobs j on j.id = r.job_id
        where r.user_to_id = :userToId and j.professional_id = :userToId
    """, nativeQuery = true)
    fun findRatingsByProfessionalId(@Param("userToId") userToId: Long): List<Rating>

}