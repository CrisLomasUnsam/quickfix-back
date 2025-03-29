package bootstrap
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProyectoApplication

fun main(args: Array<String>) {
    runApplication<ProyectoApplication>(*args)
}