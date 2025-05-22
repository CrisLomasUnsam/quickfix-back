package quickfix.dao

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.ProfessionalProfession
import java.util.*

@Component
interface ProfessionalProfessionRepository: CrudRepository<ProfessionalProfession , Long> {

    @Query("select * from professional_professions where profession_id = :professionId and professional_id = :professionalId", nativeQuery = true)
    fun findByProfessionalIdAndProfessionId(@Param("professionId") professionId: Long,@Param("professionalId") professionalId: Long): Optional<ProfessionalProfession>
}