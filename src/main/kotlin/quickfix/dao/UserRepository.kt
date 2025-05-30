package quickfix.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.dto.user.ISeeUserProfile
import quickfix.models.User
import java.time.LocalDate
import java.util.*

@Component
interface UserRepository: CrudRepository<User, Long>{

    @EntityGraph(attributePaths = ["professionalInfo", "professionalInfo.professionalProfessions", "professionalInfo.certificates"])
    fun findUserWithProfessionalInfoById(id: Long): Optional<User>

    fun findByMail(mail: String): Optional<User>

    @Query(value = "SELECT sum(price) FROM jobs WHERE professional_id = :professionalId AND status = 'DONE' AND init_date_time BETWEEN :startDate AND :endDate", nativeQuery = true)
    fun getEarningsByProfessionalIdAndDateRange(
        @Param("professionalId") professionalId: Long,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate
    ): Double?

    @Query(value = """
        SELECT u.id as id, u.name as name, u.last_name as lastName, u.verified AS verified, pr.cancellations_in_last_hour as cancellationsInLastHour,
                
                 -- Total jobs terminados
               (SELECT COUNT(*) FROM jobs j WHERE j.professional_id = u.id AND j.status = 'DONE') AS totalJobsFinished,        
                -- Promedio de score
               COALESCE((SELECT AVG(r.score) FROM ratings r WHERE r.user_to_id = u.id), 0.0) as averageRating,
                 -- Total de ratings
               (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id) as totalRatings,
                 -- Cantidad por cada score
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 1) as amountRating1,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 2) as amountRating2,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 3) as amountRating3,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 4) as amountRating4,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 5) as amountRating5
                     
        FROM users u
        INNER JOIN professionals pr ON u.professional_info_id = pr.id
        WHERE u.id = :professionalId
    """, nativeQuery = true)
    fun getSeeProfessionalProfileInfo(@Param("professionalId") professionalId: Long) : ISeeUserProfile

    @Query(value = """
        SELECT u.id as id, u.name as name, u.last_name as lastName, u.verified AS verified, u.cancellations_in_last_hour as cancellationsInLastHour,
                
                 -- Total jobs terminados
               (SELECT COUNT(*) FROM jobs j WHERE j.customer_id = u.id AND j.status = 'DONE') AS totalJobsFinished,        
                -- Promedio de score
               COALESCE((SELECT AVG(r.score) FROM ratings r WHERE r.user_to_id = u.id), 0.0) as averageRating,
                 -- Total de ratings
               (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id) as totalRatings,
                 -- Cantidad por cada score
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 1) as amountRating1,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 2) as amountRating2,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 3) as amountRating3,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 4) as amountRating4,
                (SELECT COUNT(*) FROM ratings r WHERE r.user_to_id = u.id AND r.score = 5) as amountRating5
                     
        FROM users u
        WHERE u.id = :customerId
    """, nativeQuery = true)
    fun getSeeCustomerProfileInfo(@Param("customerId") customerId: Long) : ISeeUserProfile
}