package quickfix

import quickfix.bootstrap.DataInitializer
import quickfix.dao.ProfessionalRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication

class QuickFixApp {

    @Bean
    fun dataInitializer(professionalRepository : ProfessionalRepository): DataInitializer =
        DataInitializer(professionalRepository)
    
}

fun main(args: Array<String>) {
    runApplication<QuickFixApp>(*args)
}