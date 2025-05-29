package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.dto.job.IJobCard
import quickfix.models.Job

@Component
interface JobRepository : JpaRepository<Job, Long> {

    fun existsByIdAndCustomerId(jobId: Long, customerId: Long): Boolean

    fun existsByIdAndProfessionalId(jobId: Long, professionalId: Long): Boolean

    @Query(value="select professional_id from jobs where id = :jobId fetch first 1 rows only", nativeQuery = true)
    fun findProfessionalIdByJobId(@Param("jobId") jobId: Long): Long

    @Query(value="select customer_id from jobs where id = :jobId fetch first 1 rows only", nativeQuery = true)
    fun findCustomerIdByJobId(@Param("jobId") jobId: Long): Long

    @Query(value="select * from jobs where professional_id = :professionalId and (status = 'PENDING' or status = 'IN_PROGRESS')", nativeQuery = true)
    fun findOpenJobsByProfessionalId(@Param("professionalId") professionalId: Long): Set<Job>

    @Query(
        value = """
        select 
            j.id as id,
            j.init_date_time as initDateTime,
            -- join a users como “profesional”
            u_prof.name as userName,
            u_prof.last_name as userLastName,
            u_prof.verified as userVerified,
            u_prof.id as userId,
            pr.id as professionId,
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
                and r.user_to_id = j.professional_id
            where j.customer_id = :customerId
            order by j.init_date_time desc
        """,
        nativeQuery = true)
    fun findAllJobsByCustomerId(@Param("customerId") customerId: Long, pageable: Pageable?): Page<IJobCard>


    @Query(
        value = """
        select 
            j.id as id,
            j.init_date_time as initDateTime,
            -- ahora la misma tabla users para el cliente”
            u_cust.name as userName,
            u_cust.last_name as userLastName,
            u_cust.verified as userVerified,
            u_cust.id as userId,
            pr.id as professionId,
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
                and r.user_to_id = j.customer_id
            where j.professional_id = :professionalId
            order by j.init_date_time desc
        """,
        nativeQuery = true)
    fun findAllJobsByProfessionalId(@Param("professionalId") professionalId: Long, pageable: Pageable?): Page<IJobCard>

}

