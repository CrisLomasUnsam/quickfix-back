package quickfix.dao

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Job
import quickfix.models.Profession
import java.util.Optional

@Component
interface ProfessionRepository: CrudRepository<Profession, Long> {
  
    fun findByNameIgnoreCase(name: String): Optional<Profession>

    @Query(
        value = """
        select *
           from profession p
           where LOWER(p.name) LIKE LOWER(CONCAT('%', :param, '%'))
        """,
        nativeQuery = true
    )
    fun findProfessionByFilter(@Param("param") param: String): List<Profession>

}