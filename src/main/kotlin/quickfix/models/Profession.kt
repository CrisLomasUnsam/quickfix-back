package quickfix.models

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue

@Entity
class Profession {

    @Id @GeneratedValue
    var id: Long = TODO("initialize me")

    @Column(unique = true, nullable = false, length = 50)
    lateinit var name: String
}

