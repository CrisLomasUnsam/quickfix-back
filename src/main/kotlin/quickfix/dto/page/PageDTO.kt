package quickfix.dto.page

import org.springframework.data.domain.Page
import quickfix.dto.job.JobCardDTO
import quickfix.dto.rating.RatingDTO
import quickfix.dto.rating.toDTO
import quickfix.models.Rating

data class PageDTO<T>(
    var content: List<T>,
    var first: Boolean,
    var last: Boolean,
    var empty: Boolean,
    var currentPage: Int,
    var totalPages: Int
) {
    companion object{
        fun toJobWithRatingPageDTO(page: Page<JobCardDTO>): PageDTO<JobCardDTO> {
            return PageDTO(
                content = page.content,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty,
                currentPage = page.number,
                totalPages = page.totalPages
            )
        }

        fun toRatingsDTO(page: Page<Rating>): PageDTO<RatingDTO> {
            return PageDTO(
                content = page.content.map {it.toDTO()},
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty,
                currentPage = page.number,
                totalPages = page.totalPages
            )
        }
    }
}