package quickfix.config
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.annotation.Validated

@Component
@ConfigurationProperties(prefix = "mercadopago")
class MercadoPagoProperties {
    @NotBlank
    lateinit var accessToken: String

    @NotBlank
    lateinit var backUrlBase: String // Corresponde a mercadopago.back-url-base de nuestra app
}
