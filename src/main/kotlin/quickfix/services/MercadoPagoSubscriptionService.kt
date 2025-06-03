package quickfix.services

import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import quickfix.config.MercadoPagoProperties
import quickfix.dto.mercadopago.MPAutoRecurringRequest
import quickfix.dto.mercadopago.MPSubscriptionRequestPayload
import quickfix.dto.mercadopago.MPSubscriptionResponse
import java.util.UUID

@Service
class MercadoPagoSubscriptionService(
    private val restTemplate: RestTemplate,
    private val mercadoPagoProperties: MercadoPagoProperties
) {

    private val logger = LoggerFactory.getLogger(MercadoPagoSubscriptionService::class.java)
    private val mercadoPagoApiBaseUrl = "https://api.mercadopago.com"
    private val FIXED_CURRENCY_ID = "ARS"

    // Precios definidos para los "planes"
    private val precioMensualARS = 6500.0
    private val precioAnualARS = 120000.0

    fun createSubscription(
        payerEmail: String,
        planType: String,
        externalReference: String?
    ): MPSubscriptionResponse? {

        val reason: String
        val autoRecurringData: MPAutoRecurringRequest

        when (planType.uppercase()) {
            "MENSUAL" -> {
                reason = "Suscripción Mensual QuickFix"
                autoRecurringData = MPAutoRecurringRequest(
                    frequency = 1,
                    frequencyType = "months",
                    transactionAmount = precioMensualARS,
                    currencyId = FIXED_CURRENCY_ID
                )
            }

            "ANUAL" -> {
                reason = "Suscripción Anual QuickFix"
                autoRecurringData = MPAutoRecurringRequest(
                    frequency = 1,
                    frequencyType = "years",
                    transactionAmount = precioAnualARS,
                    currencyId = FIXED_CURRENCY_ID
                )
            }

            else -> {
                logger.warn("Tipo de plan no válido recibido: {}", planType)
                throw IllegalArgumentException("Tipo de plan no válido: $planType")
            }
        }

        val subscriptionPayloadForMP = MPSubscriptionRequestPayload(
            payerEmail = payerEmail,
            status = "pending",
            reason = reason,
            externalReference = externalReference,
            backUrl = "https://www.yoursite.com",
            autoRecurring = autoRecurringData
        )

        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${mercadoPagoProperties.accessToken}")
            contentType = MediaType.APPLICATION_JSON
            set("X-Idempotency-Key", UUID.randomUUID().toString())
        }

        val entity = HttpEntity(subscriptionPayloadForMP, headers)
        logger.debug("Enviando payload a Mercado Pago /preapproval: {}", subscriptionPayloadForMP)

        return try {
            val responseEntity = restTemplate.exchange(
                "$mercadoPagoApiBaseUrl/preapproval",
                HttpMethod.POST,
                entity,
                MPSubscriptionResponse::class.java
            )
            logger.debug(
                "Suscripción iniciada para {}. ID MP: {}, InitPoint: ...{}",
                payerEmail,
                responseEntity.body?.id,
                responseEntity.body?.initPoint?.takeLast(20)
            )
            responseEntity.body
        } catch (e: HttpClientErrorException) {
            logger.error(
                "Error del CLIENTE ({}) al crear suscripción en MP para {}: {} - Cuerpo: {}",
                e.statusCode, payerEmail, e.message, e.responseBodyAsString
            )

            null
        } catch (e: HttpServerErrorException) {
            logger.error(
                "Error del SERVIDOR DE MP ({}) al crear suscripción para {}: {} - Cuerpo: {}",
                e.statusCode, payerEmail, e.message, e.responseBodyAsString
            )
            null
        } catch (e: Exception) {
            logger.error("Error GENERAL al crear suscripción en MP para {}: {}", payerEmail, e.message, e)
            null
        }
    }
}