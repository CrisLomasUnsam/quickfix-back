package quickfix.utils.searchParameters

import quickfix.models.Job
import quickfix.utils.matchJobStatusFromString
import quickfix.utils.matchProfessionFromString

data class JobSearchParameters (

    private val parameter: String

) : ISearchParameters<Job> {

    override fun matches(element: Job): Boolean =
        matchJobStatusFromString(parameter)?.let { element.status == it }
        ?: matchProfessionFromString(parameter,element)
}