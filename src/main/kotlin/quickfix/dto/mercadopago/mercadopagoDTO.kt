package quickfix.dto.mercadopago

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import quickfix.models.ProfessionalInfo


// DTO para la solicitud del cliente (frontend)
data class CreateSubscriptionClientRequest(
    val planType: String, // "MENSUAL" o "ANUAL"
)

// DTO para el estado de la suscripción(frontend)
data class SubscriptionStatusDTO(
    val subscriptionId: String?,
    val status: String,
    val nextPaymentDate: String?
) {

    companion object {
        fun toDTO(professionalInfo: ProfessionalInfo): SubscriptionStatusDTO =
            SubscriptionStatusDTO(
                subscriptionId = professionalInfo.subscriptionId,
                status = professionalInfo.subscriptionStatus.toString(),
                nextPaymentDate = professionalInfo.nextPaymentDate.toString()
            )
    }
}

// DTOs para la comunicación con Mercado Pago:
data class MPAutoRecurringRequest(
    @JsonProperty("frequency")
    val frequency: Int,
    @JsonProperty("frequency_type")
    val frequencyType: String,
    @JsonProperty("transaction_amount")
    val transactionAmount: Double,
    @JsonProperty("currency_id")
    val currencyId: String = "ARS"
)

// DTO para los webhook de respuesta de mp
data class MPBackUrls(
    @JsonProperty("success")
    val successUrl: String,
    @JsonProperty("failure")
    val failureUrl: String,
    @JsonProperty("pending")
    val pendingUrl: String
)

// DTO para el service restTemplate a mp
data class MPSubscriptionRequestPayload(
    @JsonProperty("payer_email")
    val payerEmail: String,
    @JsonProperty("status")
    val status: String = "pending",
    @JsonProperty("reason")
    val reason: String,
    @JsonProperty("external_reference")
    val externalReference: String?,
    @JsonProperty("back_url")
    val backUrl: String,
    @JsonProperty("auto_recurring")
    val autoRecurring: MPAutoRecurringRequest
)

// Para el objeto auto_recurring en la respuesta de MP
@JsonIgnoreProperties(ignoreUnknown = true)
data class MPAutoRecurringResponse(
    @JsonProperty("frequency")
    val frequency: Int?,
    @JsonProperty("frequency_type")
    val frequencyType: String?,
    @JsonProperty("transaction_amount")
    val transactionAmount: Double?,
    @JsonProperty("currency_id")
    val currencyId: String?,
    @JsonProperty("start_date")
    val startDate: String?, // MP lo añade
    @JsonProperty("end_date")
    val endDate: String?,   // MP lo puede añadir o ser null
    @JsonProperty("free_trial")
    val freeTrial: Map<String, Any>? //
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MPSubscriptionResponse(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("preapproval_plan_id") // deberia ser null
    val preapprovalPlanId: String?,
    @JsonProperty("payer_id")
    val payerId: Long?,
    @JsonProperty("payer_email") // MP va a: devolver el email vacío (si es test) o el real asociado a la cuenta MP
    val payerEmail: String?,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("init_point")
    val initPoint: String,
    @JsonProperty("back_url") // MP devuelve la back_url donde informara el estado de la suscripción con webhook
    val backUrl: String?,
    @JsonProperty("external_reference")
    val externalReference: String?,
    @JsonProperty("date_created")
    val dateCreated: String,
    @JsonProperty("last_modified")
    val lastModified: String,
    @JsonProperty("auto_recurring")
    val autoRecurring: MPAutoRecurringResponse?,
    @JsonProperty("next_payment_date")
    val nextPaymentDate: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MPSubscriptionStatusResponse(
    @JsonProperty("id")
    val id: String, // ID de la suscripción! (preapproval_id)
    @JsonProperty("payer_id")
    val payerId: Long?, // va ser null si no es usuario de mp
    @JsonProperty("payer_email")
    val payerEmail: String?, // Lo devuelve vacío si no es usuario de mp
    @JsonProperty("back_url")
    val backUrl: String?,
    @JsonProperty("collector_id")
    val collectorId: Long?,
    @JsonProperty("application_id")
    val applicationId: Long?,
    @JsonProperty("status")
    val status: String,
    @JsonProperty("reason")
    val reason: String?,
    @JsonProperty("external_reference")
    val externalReference: String?,
    @JsonProperty("date_created")
    val dateCreated: String, // Formato ISO  con timezone
    @JsonProperty("last_modified")
    val lastModified: String, // Formato ISO  con timezone
    @JsonProperty("init_point")
    val initPoint: String?, // URL para el checkout, mostrara al usuario (autenticado en mp) el estado de la suscripción
    @JsonProperty("auto_recurring")
    val autoRecurring: MPAutoRecurringResponse?,
    @JsonProperty("summarized")
    val summarized: MPSummarizedDetails?,
    @JsonProperty("next_payment_date")
    val nextPaymentDate: String?, // Recordar que viene en formato ISO con timezone
    @JsonProperty("payment_method_id")
    val paymentMethodId: String?, // "master", "visa", "amex","fisico"
    @JsonProperty("card_id")
    val cardId: String?, // ID de la tarjeta en Mercado Pago
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MPSummarizedDetails(
    @JsonProperty("quotas")
    val quotas: Int?, // Número de cuotas si aplica (generalmente para pagos, no tanto para el resumen de suscripción)

    @JsonProperty("charged_quantity")
    val chargedQuantity: Int?, // Número de cobros realizados

    @JsonProperty("pending_charge_quantity")
    val pendingChargeQuantity: Int?, // Número de cobros pendientes (si la suscripción tiene un fin)

    @JsonProperty("charged_amount")
    val chargedAmount: Double?, // Monto total cobrado

    @JsonProperty("pending_charge_amount")
    val pendingChargeAmount: Double?, // Monto total pendiente de cobrar

    @JsonProperty("semaphore")
    val semaphore: String?, // Indicador de estado, ej: "green", "yellow", "red"

    @JsonProperty("last_charged_date")
    val lastChargedDate: String?, // Formato ISO 8601

    @JsonProperty("last_charged_amount")
    val lastChargedAmount: Double?
)

data class RenewRequestDto(
    @JsonProperty("reason")
    val reason: String,

    @JsonProperty("external_reference")
    val externalReference: String,

    @JsonProperty("payer_email")
    val payerEmail: String,

    @JsonProperty("back_url")
    val backUrl: String
)
