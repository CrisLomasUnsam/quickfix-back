package quickfix.models

import jakarta.persistence.*

@Entity
@Table(name = "professions")
class Profession {

    @Id @GeneratedValue
    var id: Long = -1

    @Column(unique = true, nullable = false)
    lateinit var name: String
}

