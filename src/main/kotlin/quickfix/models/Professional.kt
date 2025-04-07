package quickfix.models

class Professional : User {

    override var id: Long = -1
    override lateinit var info: UserInfo
    var professions: Set<Profession> = mutableSetOf()
    var certificates: MutableMap<Profession, List<String>> = mutableMapOf()
    var balance: Double = 0.0
    var debt: Double = 0.0

    private fun addConfirmedJob(): Boolean = TODO("Implement me")
    private fun removeConfirmedJob(): Boolean = TODO("Implement me")

}