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
import quickfix.dto.mercadopago.MPSubscriptionStatusResponse
import quickfix.dto.mercadopago.RenewRequestDto
import quickfix.utils.enums.SubscriptionStatus
import java.util.UUID

@Service
class MercadoPagoSubscriptionService(
    private val restTemplate: RestTemplate,
    private val mercadoPagoProperties: MercadoPagoProperties,
    private val userService: UserService,
    private val subscriptionService: SubscriptionService
) {

    private val logger = LoggerFactory.getLogger(MercadoPagoSubscriptionService::class.java)
    private val mercadoPagoApiBaseUrl = "https://api.mercadopago.com"
    private val FIXED_CURRENCY_ID = "ARS"

    // Configuración de headers para la solicitud a Mercado Pago
    val headers = HttpHeaders().apply {
        set("Authorization", "Bearer ${mercadoPagoProperties.accessToken}")
        contentType = MediaType.APPLICATION_JSON
        set("X-Idempotency-Key", UUID.randomUUID().toString())
    }

    // Precios definidos para los "planes"
    private val monthPriceARS = 6500.0
    private val yearPriceARS = 120000.0

    fun createSubscription(
        currentProfessionalId: Long,
        planType: String,
    ): MPSubscriptionResponse? {

        val professionalUser = userService.getById(currentProfessionalId)
        val professionalInfo = userService.getProfessionalInfo(currentProfessionalId)
        if (professionalInfo.subscriptionId != null) {
            throw IllegalStateException(
                "El usuario ya tiene una suscripción activa. ID: ${professionalInfo.subscriptionId}"
            )
        }

        // Construccion del payload para Mercado Pago
        val payerEmail = professionalUser.mail
        val reason: String
        val autoRecurringData: MPAutoRecurringRequest

        when (planType.uppercase()) {
            "MENSUAL" -> {
                reason = "Suscripción Mensual QuickFix"
                autoRecurringData = MPAutoRecurringRequest(
                    frequency = 1,
                    frequencyType = "months",
                    transactionAmount = monthPriceARS,
                    currencyId = FIXED_CURRENCY_ID
                )
            }

            "ANUAL" -> {
                reason = "Suscripción Anual QuickFix"
                autoRecurringData = MPAutoRecurringRequest(
                    frequency = 365,
                    frequencyType = "days",
                    transactionAmount = yearPriceARS,
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
            externalReference = currentProfessionalId.toString(),
            backUrl = "https://www.yoursite.com",
            autoRecurring = autoRecurringData
        )

        val entity = HttpEntity(subscriptionPayloadForMP, headers)
        logger.info("Enviando payload a Mercado Pago /preapproval: {}", subscriptionPayloadForMP)

        // Realizar la solicitud a Mercado Pago para crear la sub
        return try {
            val responseEntity = restTemplate.exchange(
                "$mercadoPagoApiBaseUrl/preapproval",
                HttpMethod.POST,
                entity,
                MPSubscriptionResponse::class.java
            )
            logger.info(
                "Suscripción iniciada para {}. ID MP: {}, InitPoint: ...{}",
                payerEmail,
                responseEntity.body?.id,
                responseEntity.body
            )
            val responseBody = responseEntity.body
            subscriptionService.setSubscriptionStatus(
                currentProfessionalId,
                responseBody?.id!!,
                "pending",
                responseBody.nextPaymentDate!!
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

    fun getCurrentSubscriptionStatus(currentProfessionalId: Long): String {
        val professionalInfo = userService.getProfessionalInfo(currentProfessionalId)

        if (professionalInfo.subscriptionId == null) {
            professionalInfo.subscriptionStatus = SubscriptionStatus.fromString("none")!!
            return "none"
        }

        val entity = HttpEntity<String>(headers)

        return try {
            val responseEntity = restTemplate.exchange(
                "$mercadoPagoApiBaseUrl/preapproval/${professionalInfo.subscriptionId}",
                HttpMethod.GET,
                entity,
                MPSubscriptionStatusResponse::class.java
            )
            val responseBody = responseEntity.body

            subscriptionService.refreshSubscriptionStatus(
                currentProfessionalId,
                responseBody?.id!!,
                responseBody.status,
                responseBody.nextPaymentDate!!
            )

            responseBody.status
        } catch (e: HttpClientErrorException) {
            logger.error(
                "Error del CLIENTE ({}) al consultar estado de suscripción en MP para ID {}: {} - Cuerpo: {}",
                e.statusCode, professionalInfo.subscriptionId, e.message, e.responseBodyAsString
            )
            "error"
        } catch (e: HttpServerErrorException) {
            logger.error(
                "Error del SERVIDOR DE MP ({}) al consultar estado de suscripción para ID {}: {} - Cuerpo: {}",
                e.statusCode, professionalInfo.subscriptionId, e.message, e.responseBodyAsString
            )
            "error"
        } catch (e: Exception) {
            logger.error(
                "Error GENERAL al consultar estado de suscripción en MP para ID {}: {}",
                professionalInfo.subscriptionId,
                e.message,
                e
            )
            "error"
        }

    }

    fun renewSubscription(professionalId: Long): MPSubscriptionResponse? {
        val professionalUser = userService.getById(professionalId)
        val professionalInfo = userService.getProfessionalInfo(professionalId)
        if (professionalInfo.subscriptionId == null) {
            throw IllegalStateException("El usuario no tiene una suscripción activa.")
        }
        val payerEmail = professionalUser.mail

        val renewPayloadForMP = RenewRequestDto(
            payerEmail = payerEmail,
            reason = "Renovacion de suscripción QuickFix",
            externalReference = professionalId.toString(),
            backUrl = "https://www.yoursite.com",
        )

        val entity = HttpEntity(renewPayloadForMP, headers)

        logger.info("Enviando payload a Mercado Pago para renovar suscripción: {}", renewPayloadForMP)

        return try {
            val responseEntity = restTemplate.exchange(
                "$mercadoPagoApiBaseUrl/preapproval/${professionalInfo.subscriptionId}",
                HttpMethod.PUT,
                entity,
                MPSubscriptionResponse::class.java
            )
            logger.info(
                "Renovación de suscripción iniciada para {}. ID MP: {}, InitPoint: ...{}",
                payerEmail,
                responseEntity.body?.id,
                responseEntity.body
            )
            val responseBody = responseEntity.body
            if (responseBody?.status == "paused") {
                subscriptionService.setSubscriptionStatus(
                    professionalId,
                    responseBody.id,
                    "pending",
                    responseBody.nextPaymentDate!!
                )
            }

            responseEntity.body
        } catch (e: HttpClientErrorException) {
            logger.error(
                "Error del CLIENTE ({}) al renovar suscripción en MP para {}: {} - Cuerpo: {}",
                e.statusCode, payerEmail, e.message, e.responseBodyAsString
            )
            null
        } catch (e: HttpServerErrorException) {
            logger.error(
                "Error del SERVIDOR DE MP ({}) al renovar suscripción para {}: {} - Cuerpo: {}",
                e.statusCode, payerEmail, e.message, e.responseBodyAsString
            )
            null
        } catch (e: Exception) {
            logger.error("Error GENERAL al renovar suscripción en MP para {}: {}", payerEmail, e.message, e)
            null
        }
    }

}