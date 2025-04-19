package quickfix.utils.searchParameters

import quickfix.models.Job
import quickfix.utils.jobs.matchJobStatusFromString
import quickfix.utils.professions.matchProfessionFromString

data class JobSearchParameters (

    private val parameter: String

) : ISearchParameters<Job> {

    override fun matches(job: Job): Boolean =
        matchJobStatusFromString(parameter)?.let { job.status == it }
        ?: matchProfessionFromString(parameter, job)
}