package quickfix.utils.enums

import com.fasterxml.jackson.annotation.JsonValue

enum class JobStatus(val label: String) {
    PENDING("Pendiente"),
    IN_PROGRESS("En curso"),
    DONE("Terminado"),
    CANCELED("Cancelado");

    @JsonValue
    fun toValue(): String = label
}