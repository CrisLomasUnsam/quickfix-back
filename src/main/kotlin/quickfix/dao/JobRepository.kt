package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.dto.job.JobProjection
import quickfix.dto.job.MyJobProjection
import quickfix.models.Job
import quickfix.models.Rating
import quickfix.utils.enums.JobStatus

@Component
interface JobRepository : JpaRepository<Job, Long> {


    @Query(
        value = """
        select 
            j.id as id,
            j.date as date,
            -- join a users como “profesional”
            u_prof.name as userName,
            u_prof.last_name as userLastName,
            pr.name as profession,
            j.status as status,
            j.price as price,
            coalesce(r.score, 0) as score
            from jobs j
            -- tabla de usuarios para el profesional
            join users u_prof
                on u_prof.id = j.professional_id
            -- tabla de profesiones
            join professions pr
                on pr.id = j.profession_id
             -- rating
            left join ratings r 
                on r.job_id = j.id
            where j.customer_id = :customerId
            order by j.date asc
        """,
        nativeQuery = true)
    fun findAllByCustomerId(@Param("customerId") customerId: Long, pageable: Pageable): Page<MyJobProjection>


    @Query(
        value = """
        select 
            j.id as id,
            j.date as date,
            -- ahora la misma tabla users para el cliente”
            u_cust.name as userName,
            u_cust.last_name as userLastName,
            pr.name as profession,
            j.status as status,
            j.price as price,
            coalesce(r.score, 0) as score
            from jobs j
            join users u_cust
                on u_cust.id = j.customer_id
            join professions pr
                on pr.id = j.profession_id
            left join ratings r 
                on r.job_id = j.id
            where j.professional_id = :professionalId
            order by j.date asc
        """,
        nativeQuery = true)
    fun findAllByProfessionalId(@Param("professionalId") professionalId: Long, pageable: Pageable): Page<MyJobProjection>

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
        select j.id,
               j.date,
               u.name AS userName,
               u.last_name AS userLastName,
               p.name AS profession,
               j.status,
               j.price,
               r.score
           from jobs j
            join users u
                on u.id = j.professional_id
            join professions p
                on p.id = j.profession_id
            left join ratings r 
                on r.job_id = j.id
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
    fun findJobByFilter(@Param("customerId") customerId: Long, @Param("param") param: String?) : List<JobProjection>

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

    fun countByProfessionalIdAndStatus(professionalId: Long, status: JobStatus = JobStatus.DONE): Int

}

