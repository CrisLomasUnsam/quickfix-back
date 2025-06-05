package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.utils.enums.SubscriptionStatus
import java.time.OffsetDateTime

@Service
class SubscriptionService(
    private val userService: UserService
) {
    @Transactional(rollbackFor = [Exception::class])
    fun setSubscriptionStatus(
        currentProfessionalId: Long,
        subscriptionId: String,
        status: String,
        nextPaymentDate: String
    ) {
        val professionalInfo = userService.getProfessionalInfo(currentProfessionalId)

        professionalInfo.subscriptionId = subscriptionId
        professionalInfo.subscriptionStatus = SubscriptionStatus.fromString(status)!!
        professionalInfo.nextPaymentDate = OffsetDateTime.parse(nextPaymentDate).toLocalDateTime()
    }

    @Transactional(rollbackFor = [Exception::class])
    fun refreshSubscriptionStatus(
        currentProfessionalId: Long,
        subscriptionId: String,
        status: String,
        nextPaymentDate: String?
    ) {
        val professionalInfo = userService.getProfessionalInfo(currentProfessionalId)

        println("Refreshing subscription status for user ID: $currentProfessionalId, subscription ID: $subscriptionId, status: $status, next payment date: $nextPaymentDate")

        if (professionalInfo.subscriptionId == null || professionalInfo.subscriptionId != subscriptionId) {
            throw IllegalStateException("No se encontró una suscripción activa con ID: $subscriptionId")
        }

        // Si vencio la suscripción, mp no devuelve nextPaymentDate, pero el id sigue siendo valido(PAUSED)
        if (nextPaymentDate.isNullOrBlank()) {
            professionalInfo.subscriptionStatus = SubscriptionStatus.fromString(status)!!
            return
        }

        // Si la suscripción fue cancelada, se limpia el ID y se marca como "none", guardamos la fecha para no asignar un trial.
        if (status == "cancelled") {
            professionalInfo.subscriptionId = null
            professionalInfo.subscriptionStatus = SubscriptionStatus.fromString("none")!!
            return
        }

        // Si la suscripción está activa, simplemente actualizamos la fecha de pago y el estado(puede estar pausada o activa
        professionalInfo.nextPaymentDate = OffsetDateTime.parse(nextPaymentDate).toLocalDateTime()
        professionalInfo.subscriptionStatus = SubscriptionStatus.fromString(status)!!
    }
}