package quickfix.utils.jobs

import quickfix.dto.job.jobOffer.JobOfferDTO
import quickfix.models.Job
import quickfix.models.TimeUnit
import quickfix.utils.INSTANT_REQUEST_LIVE_DAYS
import quickfix.utils.enums.JobStatus
import quickfix.utils.functions.hasMatchingStart
import java.time.Duration
import java.time.LocalDateTime

fun dateTimesCollides(startDt1 : LocalDateTime, endDt1 : LocalDateTime, startDt2 : LocalDateTime, endDt2 : LocalDateTime) : Boolean =
    startDt1 < endDt2 && startDt2 < endDt1

fun getJobRequestKey(professionId: Long, customerId: Long) : String =
    "JobRequest_${professionId}_${customerId}_"

fun getJobOfferKey(professionId: Long, customerId: Long, professionalId: Long) : String =
    "JobOffer_${professionId}_${customerId}_${professionalId}_"

fun getJobEndtime(job : Job) : LocalDateTime {
    val jobDuration = job.duration * TimeUnit.fromLabel(job.durationUnit)!!.minutes
    return job.initDateTime.plusMinutes(jobDuration)
}

fun getJobOfferEndtime(jobOffer : JobOfferDTO) : LocalDateTime {
    val jobDuration = jobOffer.jobDuration * TimeUnit.fromLabel(jobOffer.jobDurationTimeUnit)!!.minutes
    return jobOffer.neededDatetime.plusMinutes(jobDuration)
}

//If it is a future request, we set a TTL to cancel it automatically as soon as the date and time of need for the service arrives.
fun getJobRequestTTLForRedis(neededDatetime: LocalDateTime, instantRequest : Boolean) : Duration {
    return if(instantRequest)
        Duration.ofDays(INSTANT_REQUEST_LIVE_DAYS)
    else
        Duration.between(LocalDateTime.now(), neededDatetime).abs()
}

//If it is a future request, we set a TTL to cancel it automatically as soon as the date and time of need for the service arrives.
fun getJobOfferTTLForRedis(neededDatetime: LocalDateTime, instantRequest : Boolean) : Duration {
    val instantRequestDaysLeft = Duration.between(neededDatetime, LocalDateTime.now().plusDays(INSTANT_REQUEST_LIVE_DAYS)).toDays()
    return if(instantRequest)
        Duration.ofDays(instantRequestDaysLeft)
    else
        Duration.between(LocalDateTime.now(), neededDatetime).abs()
}

fun matchJobStatusFromString(param: String): JobStatus? {
    val cleanedParam = param.trim().lowercase()
    return jobStatusMapping.entries.find { (key, _) ->
        cleanedParam.contains(key) ||
                key.contains(cleanedParam) ||
                hasMatchingStart(cleanedParam, key)
    }?.value
}

private val jobStatusMapping = mapOf(
    "finalizados" to JobStatus.DONE,
    "pendientes" to JobStatus.PENDING,
    "en curso" to JobStatus.IN_PROGRESS,
    "cancelados" to JobStatus.CANCELED
)