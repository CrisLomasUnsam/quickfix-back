package quickfix.models

import jakarta.persistence.*
import quickfix.utils.enums.ProfessionTypes

@Entity
class Profession {

    @Id @GeneratedValue
    var id: Long = -1

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    lateinit var professionType: ProfessionTypes
}

