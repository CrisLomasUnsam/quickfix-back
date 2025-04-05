package quickfix.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem
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
            .group("v1")  // nombre del grupo
            .packagesToScan("quickfix.controllers")  // solo estos paths se documentan
            .build()
    }
//
//    private fun getApiInfo(): ApiInfo {
//        val contact = Contact("Hendi Santika", "http://hendisantika.wordpress.com", "hendisantika@gmail.com")
//        return ApiInfoBuilder()
//            .title("Example Api Title")
//            .description("Example Api Definition")
//            .version("1.0.0")
//            .license("Apache 2.0")
//            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
//            .contact(contact)
//            .build()
//    }

//    @Bean
//    fun publicApi(): GroupedOpenApi {
//        return GroupedOpenApi.builder()
//            .group("base-service")
//            .pathsToMatch("/**")
//            .build()
//    }
//
//    @Bean
//    fun customOpenAPI(
//        @Value("\${application-description}") appDescription: String?, @Value(
//            "\${application-version}"
//        ) appVersion: String?
//    ): OpenAPI {
//        val contact = Contact()
//        contact.email = "hendisantika@yahoo.co.id"
//        contact.name = "HENDI SANTIKA"
//        contact.url = "https://www.s.id/hendisantika"
//        return OpenAPI()
//            .info(
//                Info()
//                    .title("Microservice Base Service Application API")
//                    .version(appVersion)
//                    .description(appDescription)
//                    .termsOfService("http://swagger.io/terms/")
//                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
//                    .contact(contact)
//            )
//    }

}