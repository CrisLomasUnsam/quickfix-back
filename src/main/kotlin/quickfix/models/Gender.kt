package quickfix.models

import quickfix.utils.exceptions.IllegalDataException

enum class Gender(val genderName: String, val altValue: String) {
    MALE(genderName = "MALE", altValue = "Masculino"),
    FEMALE(genderName = "FEMALE", altValue = "Feminino"),
    OTHER(genderName = "OTHER", altValue = "Otro");

    companion object {
        fun fromName(nombre: String): Gender =
            entries.find {
                it.genderName.equals(nombre, ignoreCase = true) ||
                it.altValue.equals(nombre, ignoreCase = true)
            }
                ?: throw IllegalDataException("Error en parseo de género")
    }
}