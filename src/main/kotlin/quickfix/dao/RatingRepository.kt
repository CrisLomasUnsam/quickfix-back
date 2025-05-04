package quickfix.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Component
import quickfix.models.Rating

@Component
interface RatingRepository : PagingAndSortingRepository<Rating, Long>, CrudRepository<Rating, Long> {

    fun findByUserToId(userId: Long, pageable: Pageable): Page<Rating>

    fun findByUserFromId(userId: Long, pageable: Pageable): Page<Rating>
}