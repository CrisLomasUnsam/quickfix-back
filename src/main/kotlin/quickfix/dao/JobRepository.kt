package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Job

@Component
class JobRepository: Repository<Job>() {

    fun setToDone(id: Long){
        TODO("Not yet implemented")
    }

    fun setToCancelled(id: Long){
        TODO("Not yet implemented")
    }
}