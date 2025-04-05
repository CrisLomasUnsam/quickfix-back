package quickfix

import quickfix.bootstrap.DataInitializer
import quickfix.dao.ProfessionalRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import quickfix.dao.CustomerRepository

@SpringBootApplication

class QuickFixApp {

    @Bean
    fun dataInitializer(
        professionalRepository : ProfessionalRepository,
        customerRepository : CustomerRepository
    ): DataInitializer =
        DataInitializer(professionalRepository, customerRepository)
    
}

fun main(args: Array<String>) {
    runApplication<QuickFixApp>(*args)
}