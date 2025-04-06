package quickfix.models

import quickfix.utils.exceptions.BusinessException

data class JobRequest(
    val customer: Customer,
    val profession: Profession,
    val detail: String
) {
    fun validate() {

        customer.validate()
        if (!validProfession(profession)) throw BusinessException("Profession is not valid")
        if (!validDetail(detail)) throw BusinessException("Detail is empty")
    }

    private fun validProfession(profession: Profession): Boolean = profession.toString().isNotBlank()
    private fun validDetail(detail: String):Boolean = detail.isNotBlank()
}
