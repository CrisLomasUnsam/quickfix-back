package quickfix.dto.mercadopago

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties


// DTO para la solicitud del cliente (frontend)
data class CreateSubscriptionClientRequest(
    val planType: String, // "MENSUAL" o "ANUAL"
)

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