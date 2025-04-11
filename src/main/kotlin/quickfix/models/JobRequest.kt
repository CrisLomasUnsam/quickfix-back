package quickfix.models

import quickfix.utils.exceptions.BusinessException

data class JobRequest(
    val customer: User,
    val profession: Profession,
    val detail: String,
    val address: Address
) {
    fun validate() {

        customer.validate()
        if (!validProfession(profession))
            throw BusinessException("La profesión no es válida")
        if (!validDetail(detail))
            throw BusinessException("Detalle está vacío")
    }

    private fun validProfession(profession: Profession): Boolean = profession.toString().isNotBlank()
    private fun validDetail(detail: String):Boolean = detail.isNotBlank()
}
