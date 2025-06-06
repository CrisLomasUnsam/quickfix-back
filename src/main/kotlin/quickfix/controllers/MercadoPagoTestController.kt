package quickfix.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import quickfix.services.MercadoPagoSubscriptionService
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.mercadopago.CreateSubscriptionClientRequest
import quickfix.dto.mercadopago.MPSubscriptionResponse
import quickfix.services.SubscriptionService

@RestController
@RequestMapping("/mp/subscriptions")
class SubscriptionController(
    private val mercadoPagoSubscriptionService: MercadoPagoSubscriptionService,
    private val subscriptionService: SubscriptionService
) {
    private val logger = LoggerFactory.getLogger(SubscriptionController::class.java)

    @ModelAttribute("currentProfessionalId")
    fun getCurrentProfessionalId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @PostMapping("/create")
    fun createNewSubscription(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long,
        @RequestBody clientRequest: CreateSubscriptionClientRequest
    ): ResponseEntity<*> {
        if (clientRequest.planType.isBlank()) {
            logger.warn("Intento de crear suscripción con datos incompletos: {}", clientRequest)
            return ResponseEntity.badRequest().body(mapOf("error" to "Email y tipo de plan son requeridos."))
        }

        logger.info("Recibida solicitud para crear suscripción: {}", clientRequest)

        try {
            val subscriptionResponse: MPSubscriptionResponse? = mercadoPagoSubscriptionService.createSubscription(
                currentProfessionalId,
                planType = clientRequest.planType,
            )

            return if (subscriptionResponse?.initPoint?.isNotBlank() == true) {
                logger.info("Suscripción iniciada en MP, devolviendo init_point. MP ID: {}", subscriptionResponse.id)
                ResponseEntity.ok(
                    mapOf(
                        "init_point" to subscriptionResponse.initPoint,
                        "subscription_id" to subscriptionResponse.id
                    )
                )
            } else {
                logger.error("Servicio de suscripción de MP devolvió null o init_point vacío para {}", clientRequest)
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("error" to "Error al iniciar el proceso de suscripción con Mercado Pago."))
            }
        } catch (e: IllegalArgumentException) {
            logger.warn("Error de datos inválidos al crear suscripción para {}: {}", clientRequest, e.message)
            return ResponseEntity.badRequest().body(mapOf("error" to e.message))
        } catch (e: Exception) {
            logger.error(
                "Error inesperado en SubscriptionController al crear suscripción para {}: {}",
                clientRequest,
                e.message,
                e
            )
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error inesperado en el servidor al procesar la suscripción."))
        }
    }

    @GetMapping("/status")
    fun getCurrentSubscriptionStatus(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long
    ): ResponseEntity<*> {
        return try {
            val status = mercadoPagoSubscriptionService.getCurrentSubscriptionStatus(currentProfessionalId)
            ResponseEntity.ok(mapOf("status" to status))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error al consultar el estado de la suscripción."))
        }
    }

    @PutMapping("/renew")
    fun renewSubscription(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long
    ): ResponseEntity<*> {
        return try {
            val renewalResponse: MPSubscriptionResponse? =
                mercadoPagoSubscriptionService.renewSubscription(currentProfessionalId)
            if (renewalResponse != null) {
                ResponseEntity.ok(
                    mapOf(
                        "subscription_id" to renewalResponse.id,
                        "init_point" to renewalResponse.initPoint,
                    )
                )
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(mapOf("error" to "No se pudo renovar la suscripción."))
            }
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error al renovar la suscripción."))
        }
    }

    @PutMapping("/startFreeTrial")
    fun startFreeTrial(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long
    ): ResponseEntity<*> {
        return try {
            val trialResponse = subscriptionService.initFreeTrial(currentProfessionalId)
            ResponseEntity.ok(trialResponse)
        } catch (e: Exception) {
            logger.error("Error al iniciar el período de prueba para el profesional ID $currentProfessionalId: ${e.message}", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error al iniciar el período de prueba."))
        }
    }

    @PutMapping("/endFreeTrial")
    fun endSubscription(
        @ModelAttribute("currentProfessionalId") currentProfessionalId: Long
    ): ResponseEntity<*> {
        return try {
            subscriptionService.endSubscription(currentProfessionalId)
            ResponseEntity.ok(mapOf("message" to "Período de prueba finalizado correctamente."))
        } catch (e: Exception) {
            logger.error("Error al finalizar el período de prueba para el profesional ID $currentProfessionalId: ${e.message}", e)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to "Error al finalizar el período de prueba."))
        }
    }


    // WebHooks(mmmm)
    // ej. /mp-callbacks/success, /mp-callbacks/failure, /mp-callbacks/pending
    @GetMapping("/mp-callbacks/success")
    fun handleSuccessCallback(
        @RequestParam params: Map<String, String>
        // @RequestParam("collection_id") collectionId: String?,
        // @RequestParam("collection_status") collectionStatus: String?,
        // @RequestParam("payment_id") paymentId: String?,
        // @RequestParam("status") status: String?,
        // @RequestParam("external_reference") externalReference: String?,
        // @RequestParam("payment_type") paymentType: String?,
        // @RequestParam("merchant_order_id") merchantOrderId: String?,
        // @RequestParam("preference_id") preferenceId: String?, // Si es de una preferencia de pago
        // @RequestParam("site_id") siteId: String?,
        // @RequestParam("processing_mode") processingMode: String?,
        // @RequestParam("merchant_account_id") merchantAccountId: String?,
        // @RequestParam("preapproval_id") preapprovalId: String? // Para suscripciones
    ): ResponseEntity<String> {
        logger.info("Callback de ÉXITO de Mercado Pago recibido: {}", params)
        val preapprovalId = params["preapproval_id"]
        val externalReference = params["external_reference"] // Si lo enviaste en el back_url
        // TODO:
        // 1. Buscar la suscripción en la db por externalReference o preapprovalId, recordar que el externalReference es el id del profesional.
        // 2. Consultar el estado real de la suscripción a MP usando el preapprovalId, para verificar el estado si no podemos implementar webhooks
        // 3. Actualizar el estado de la suscripción del User en su tabla.
        //    (ej. marcarla como ACTIVE, setear subscriptionCurrentPeriodEndDate)
        // 4. Esta debe coincidir con la finalizacion del free trial, manejar eso
        // 5. Redirigir al usuario a una página de éxito en el front(o toast).
        return ResponseEntity.ok("Éxito - Suscripción procesada (ID MP: $preapprovalId). External Ref: $externalReference. Ver como manejar en el front y dba.")
    }

    @GetMapping("/mp-callbacks/failure")
    fun handleFailureCallback(@RequestParam params: Map<String, String>): ResponseEntity<String> {
        logger.warn("Callback de FALLO de Mercado Pago recibido: {}", params)
        // TODO:
        // 1. Registrar el fallo.
        // 2. Actualizar estado en la DB (ej. PENDING_PAYMENT_FAILED).
        // 3. Redirigir al usuario a una página de fallo en el front(o toast).
        return ResponseEntity.badRequest()
            .body("Fallo - La suscripción no pudo completarse. Ver como manejar en el front.")
    }

    @GetMapping("/mp-callbacks/pending")
    fun handlePendingCallback(@RequestParam params: Map<String, String>): ResponseEntity<String> {
        logger.info("Callback PENDIENTE de Mercado Pago recibido: {}", params)
        // TODO:
        // 1. Registrar el estado pendiente.
        // 2. Actualizar estado en la DB.
        // 3. Redirigir al usuario a una página de estado pendiente en el front(o toast).
        return ResponseEntity.ok("Pendiente - La suscripción está pendiente de procesamiento. Manejar en el front.")
    }
}