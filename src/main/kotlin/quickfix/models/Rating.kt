package quickfix.models

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Rating : Identifier {

    @Id @GeneratedValue
    override var id: Long = -1

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var userFrom: User

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var userTo: User

    @OneToOne(cascade = [(CascadeType.ALL)])
    lateinit var job: Job
    
    var score: Int = 0
    lateinit var yearAndMonth: LocalDate
    lateinit var comment: String

    override fun validate() {
        TODO("Not yet implemented")
    }
}
