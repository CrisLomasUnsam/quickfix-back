package quickfix.dto.job

import org.springframework.data.domain.Page

data class PageDTO<T>(
    var content: MutableList<T>,
    var first: Boolean,
    var last: Boolean,
    var empty: Boolean,
    var totalPages: Int
) {
    companion object{
        fun toDTO(page: Page<JobDTO>): PageDTO<JobDTO> {
            return PageDTO(
                content = page.content,
                first = page.isFirst,
                last = page.isLast,
                empty = page.isEmpty,
                totalPages = page.totalPages
            )
        }
    }
}