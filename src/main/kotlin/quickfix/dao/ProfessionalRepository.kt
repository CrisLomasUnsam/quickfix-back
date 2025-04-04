package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Professional

@Component
class ProfessionalRepository: Repository<Professional>() {

    fun findByMail(mail: String): Professional? =
        this.elements.find { it.mail == mail }

}