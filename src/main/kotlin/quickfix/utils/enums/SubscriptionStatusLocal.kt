package quickfix.utils.enums

enum class SubscriptionStatusLocal {
    NONE,                // Sin suscripción o prueba
    APP_FREE_TRIAL,      // Período de prueba gestionado por nosotros
    MP_PENDING_AUTHORIZATION, // Esperando que el usuario complete el init_point
    MP_ACTIVE,           // Suscripción activa y pagada en Mercado Pago
    MP_PAYMENT_FAILED,   // Falló el último intento de cobro en MP
    MP_PAUSED,           // Pausada en MP
    CANCELLED_BY_USER,   // Cancelada por el usuario desde la app
    CANCELLED_BY_MP,     // Cancelada por MP (ej. fraude, problemas de pago persistentes)
    ENDED                // Term
}