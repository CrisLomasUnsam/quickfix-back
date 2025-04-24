package quickfix.models

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RegisterToken: Identifier {

    @Id @GeneratedValue
    override var id: Long = -1


    @OneToOne
    @JoinColumn(nullable = false)
    lateinit var user: User

    lateinit var token: String
    var expiryDate: LocalDateTime = LocalDateTime.now().plusMinutes(60)

    override fun validate() {}
}