package bootstrap
import dao.ProfessionalRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["dao", "bootstrap", "models"])
class QuickFixApp {

    @Bean
    fun dataInitializer(professionalRepository : ProfessionalRepository)
    : DataInitializer {
        return DataInitializer(professionalRepository)
    }
}

fun main(args: Array<String>) {
    runApplication<QuickFixApp>(*args)
}