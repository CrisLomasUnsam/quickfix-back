package quickfix.dto.page

import org.springframework.data.domain.Page
import quickfix.dto.job.JobDTO
import quickfix.dto.job.MyJobDTO

data class PageDTO<T>(
    var content: MutableList<T>,
    var first: Boolean,
    var last: Boolean,
    var empty: Boolean,
    var currentPage: Int,
    var totalPages: Int
) {
    companion object{
        fun toDTO(page: Page<MyJobDTO>): PageDTO<MyJobDTO> {
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