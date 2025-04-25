package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Rating
import java.util.*

@Component
interface RatingRepository : CrudRepository<Rating, Long> {

    fun findByUserToId(userId: Long): Optional<List<Rating>>

    fun findByUserFromId(userId: Long): Optional<List<Rating>>


}