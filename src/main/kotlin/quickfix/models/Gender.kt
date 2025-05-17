package quickfix.models

import quickfix.utils.exceptions.IllegalDataException

enum class Gender(val genderName: String) {
    MALE(genderName = "MALE"),
    FEMALE(genderName = "FEMALE"),
    OTHER(genderName = "OTHER");

    companion object {
        fun fromName(nombre: String): Gender =
            entries.find { it.genderName.equals(nombre, ignoreCase = true) }
                ?: throw IllegalDataException("Error en parseo de género")
    }
}