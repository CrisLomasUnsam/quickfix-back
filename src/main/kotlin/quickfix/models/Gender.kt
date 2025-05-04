package quickfix.models

import quickfix.utils.exceptions.BusinessException

enum class Gender(val nombre: String) {
    MALE(nombre = "MALE"),
    FEMALE(nombre = "FEMALE"),
    OTHER(nombre = "OTHER");

    companion object {
        fun fromNombre(nombre: String): Gender =
            entries.find { it.nombre.equals(nombre, ignoreCase = true) }
                ?: throw BusinessException("Error en parseo a género")
    }
}