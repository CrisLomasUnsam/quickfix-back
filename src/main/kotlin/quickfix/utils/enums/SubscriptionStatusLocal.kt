package quickfix.utils.enums

enum class SubscriptionStatus {
    NONE,
    PENDING,
    AUTHORIZED,
    PAUSED;

    companion object {
        fun fromString(status: String): SubscriptionStatus? {
            return SubscriptionStatus.entries.find { it.name.equals(status, ignoreCase = true) }
        }
    }
}