package quickfix

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import quickfix.config.MercadoPagoProperties

@SpringBootApplication
@EnableConfigurationProperties(MercadoPagoProperties::class)

class QuickFixApp

fun main(args: Array<String>) {
    runApplication<QuickFixApp>(*args)
}