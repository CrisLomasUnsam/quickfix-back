package quickfix.utils.searchParameters

import quickfix.models.Job
import quickfix.utils.jobs.matchJobStatusFromString
import quickfix.utils.professions.matchProfessionFromString

data class JobSearchParameters (

    private val parameter: String

) : ISearchParameters<Job> {

    override fun matches(element: Job): Boolean =
        matchJobStatusFromString(parameter)?.let { element.status == it }
        ?: matchProfessionFromString(parameter, element)
}