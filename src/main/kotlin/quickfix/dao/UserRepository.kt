package quickfix.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.User
import java.time.LocalDate
import java.util.*

@Component
interface UserRepository: CrudRepository<User, Long>{

    @EntityGraph(attributePaths = ["professionalInfo.professions"])
    fun findUserProfessionsById(id: Long): Optional<User>

    @EntityGraph(attributePaths = ["professionalInfo", "professionalInfo.professions", "professionalInfo.certificates"])
    fun findUserWithProfessionalInfoById(id: Long): Optional<User>

    fun findByMail(mail: String): User?
  
    fun findByDni(dni: Int): User?

    @Query(value = """
        SELECT sum(j.price)
        FROM jobs j
        WHERE j.professional_id = :professionalId
        AND j.status = 'DONE'
        AND j.date BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    fun getEarningsByProfessionalIdAndDateRange(
        @Param("professionalId") professionalId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Double?
}