package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Job

@Component
class JobRepository: Repository<Job>() {

}