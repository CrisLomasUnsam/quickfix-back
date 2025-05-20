package quickfix.models

enum class TimeUnit(val label: String, val minutes: Long) {
    MINUTE("Minutos", 1),
    HOUR("Horas", 60),
    DAY("Dias", 1440),
    WEEK("Semanas", 10080),
    MONTH("Meses", 40320); // 28 días - 4 semanas

    companion object {
        fun fromLabel(label: String): TimeUnit? {
            return entries.firstOrNull { it.label.equals(label, ignoreCase = true) }
        }
    }
}