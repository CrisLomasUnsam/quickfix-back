package quickfix.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        val server = Server()
        server.url = "http://localhost:8080/"
        server.description = "QuickFix Back"

        return OpenAPI()
            .info(
                Info()
                    .title("QuickFix API")
                    .version("1.0.1")
                    .description("Endpoints y modelos (schemas)")
            )
            .servers(listOf(server))
    }

    @Bean
    fun apiV1Group(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("v1")
            .packagesToScan("quickfix.controllers")
            .build()
    }
}