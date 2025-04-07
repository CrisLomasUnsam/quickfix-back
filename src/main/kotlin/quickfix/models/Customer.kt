package quickfix.models

class Customer : User {

    override var id: Long = -1
    override lateinit var info: UserInfo

}