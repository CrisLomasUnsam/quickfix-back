package quickfix.utils

import quickfix.models.Job
import quickfix.models.Professional

data class JobSearchParameters (
    val professional: Professional? = null,
    val done: Boolean? = null,
    val inProgress: Boolean? = null,

) : SearchParameters<Job> {

    override fun matches(element: Job): Boolean {
        return  (professional == null || element.professional == professional) &&
                (done == null || element.done == done) &&
                (inProgress == null || element.inProgress == inProgress)
    }
}

