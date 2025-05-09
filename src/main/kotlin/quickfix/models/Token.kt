package quickfix.models

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
class Token: Identifier {

    @Id @GeneratedValue
    override var id: Long = -1


    @OneToOne
    @JoinColumn(nullable = false)
    lateinit var user: User

    private lateinit var token: String
    var expiryDate: LocalDateTime = LocalDateTime.now().plusMinutes(60)

    override fun validate() {}

    companion object{
        fun createTokenEntity(user: User) =
            Token().apply {
                this.user = user
                this.token = UUID.randomUUID().toString() }
    }
}