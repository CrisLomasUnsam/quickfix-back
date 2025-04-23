package quickfix.dao

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.utils.enums.JobStatus
import quickfix.utils.searchParameters.ISearchParameters
import quickfix.utils.exceptions.BusinessException

@Component
interface JobRepository : CrudRepository<Job, Long> {

    fun findAllByCustomerId(customerId: Long): List<Job>

    fun findAllByProfessionalId(professionalId: Long): List<Job>




//
//    fun setToCancelled(id: Long){
//        TODO("Not yet implemented")
//    }

    @Query(
        value = """
        select *
           from job j
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
    fun findJobByFilter(
        @Param("customerId") customerId: Long,
        @Param("param") param: String?
    ) : List<Job>

//    fun getAllByUserId(customerId: Long): List<Job> =
//        this.findAll()
//            .filter { it.customer.id == customerId }
//            .ifEmpty { throw BusinessException("No existen servicios pertenecientes al cliente.") }
}