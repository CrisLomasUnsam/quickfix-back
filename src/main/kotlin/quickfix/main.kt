package quickfix

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication

class QuickFixApp

fun main(args: Array<String>) {
    runApplication<QuickFixApp>(*args)
}