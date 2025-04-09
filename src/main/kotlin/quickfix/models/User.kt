package quickfix.models

interface User : Id {

    companion object {
        const val EDAD_REQUERIDA = 18
    }

    var info : UserInfo

    override fun validate() = info.validate()

}