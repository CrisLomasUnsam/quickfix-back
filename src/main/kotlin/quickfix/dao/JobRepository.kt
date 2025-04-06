package quickfix.dao

import org.springframework.stereotype.Component
import quickfix.models.Job

@Component
class JobRepository: Repository<Job>() {

    fun setToDone(id: Int){
        TODO("Not yet implemented")
    }

    fun setToCancelled(id: Int){
        TODO("Not yet implemented")
    }
}