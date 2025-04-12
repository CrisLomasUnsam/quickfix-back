package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Rating
@Component
interface RatingRepository : CrudRepository<Rating, Long>