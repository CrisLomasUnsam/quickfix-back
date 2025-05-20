package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Job

@Component
interface JobRepository : JpaRepository<Job, Long> {

    fun findAllByCustomerId(customerId: Long, pageable: Pageable): Page<Job>

    fun findAllByProfessionalId(professionalId: Long, pageable: Pageable): Page<Job>

    fun existsByIdAndCustomerId(jobId: Long, customerId: Long): Boolean

    fun existsByIdAndProfessionalId(jobId: Long, professionalId: Long): Boolean

    @Query(value="""
        select professional_id from jobs where id = :jobId fetch first 1 rows only
    """, nativeQuery = true)
    fun findProfessionalIdByJobId(@Param("jobId") jobId: Long): Long

    @Query(value="""
        select customer_id from jobs where id = :jobId fetch first 1 rows only
    """, nativeQuery = true)
    fun findCustomerIdByJobId(@Param("jobId") jobId: Long): Long

    @Query(value="""
        select * from jobs where professional_id = :professionalId and (status = 'PENDING' or status = 'IN_PROGRESS')
    """, nativeQuery = true)
    fun findOpenJobsByProfessionalId(@Param("professionalId") professionalId: Long): Set<Job>

    @Query(value = """
        select *
           from jobs j
           where j.customer_id = :customerId
            and (
                :param is null
                or j.id::text              ilike concat('%', :param, '%')
                or j.status                ilike concat('%', :param, '%')
                or j.profession_id::text   ilike concat('%', :param, '%')
                or j.professional_id::text ilike concat('%', :param, '%')
            )
            """, nativeQuery = true)
    fun findJobByFilter(@Param("customerId") customerId: Long, @Param("param") param: String?) : List<Job>
}

