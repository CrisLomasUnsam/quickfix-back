package quickfix.models

import java.time.LocalDate

class Rating {
    lateinit var userFrom: User
    lateinit var userTo: User
    lateinit var job: Job
    var score: Int = 0
    lateinit var yearAndMonth: LocalDate
    lateinit var comment: String
}
