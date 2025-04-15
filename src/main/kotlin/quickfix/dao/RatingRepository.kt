package quickfix.dao

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Rating
import java.util.*

@Component
interface RatingRepository : CrudRepository<Rating, Long> {

    @EntityGraph(attributePaths = ["userFrom","userTo","job"])
    fun findByUserToId(userId: Long): Optional<List<Rating>>

    @EntityGraph(attributePaths = ["userFrom","userTo","job"])
    fun findByUserFromId(userId: Long): Optional<List<Rating>>
}