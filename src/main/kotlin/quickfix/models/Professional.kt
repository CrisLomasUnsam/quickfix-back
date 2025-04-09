package quickfix.models

class Professional : Id {

    override var id: Long = -1

    var professions: Set<Profession> = mutableSetOf()
    var certificates: MutableMap<Profession, List<String>> = mutableMapOf()
    var balance: Double = 0.0
    var debt: Double = 0.0

    override fun validate() {
    }

    private fun addConfirmedJob(): Boolean = TODO("Implement me")
    private fun removeConfirmedJob(): Boolean = TODO("Implement me")

}