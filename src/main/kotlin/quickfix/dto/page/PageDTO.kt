package quickfix.dto.page

import org.springframework.data.domain.Page
import quickfix.dto.job.JobWithRatingDTO

data class PageDTO<T>(
    var content: MutableList<T>,
    var first: Boolean,
    var last: Boolean,
    var empty: Boolean,
    var currentPage: Int,
    var totalPages: Int
) {
    companion object{
        fun toJobWithRatingPageDTO(page: Page<JobWithRatingDTO>): PageDTO<JobWithRatingDTO> {
            return PageDTO(
                content = page.content,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty,
                currentPage = page.number,
                totalPages = page.totalPages
            )
        }
    }
}