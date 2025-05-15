package quickfix.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import quickfix.models.Role
import quickfix.security.JwtAuthFilter
import quickfix.utils.FRONTEND_URL

//import org.springframework.security.web.csrf.CookieCsrfTokenRepository
//import quickfix.security.CsrfTokenRequestHandler

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    lateinit var jwtAuthFilter: JwtAuthFilter

    @Bean @Throws(Exception::class)
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager = authConfig.authenticationManager

    @Bean
    fun corsConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins(FRONTEND_URL)
                    .allowedHeaders("*")
                    .allowedMethods("POST", "GET","PATCH", "DELETE", "PUT")
                    .allowCredentials(true)
            }
        }
    }

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors { it.disable() }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/quickfix-api/**",
                    "/swagger",
                    "/swagger-ui/**",
                    "/registration",
                    "/registration/confirm",
                    "/login/**",
                    "/recovery",
                    "/recovery/confirm",
                    "/error"
                    ).permitAll()

                it.requestMatchers(HttpMethod.OPTIONS).permitAll()

                it.requestMatchers(HttpMethod.POST, "/chat").hasAnyAuthority(Role.CUSTOMER.name,Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.GET, "/chat/**").hasAnyAuthority(Role.CUSTOMER.name,Role.PROFESSIONAL.name)

                it.requestMatchers(HttpMethod.GET, "/job/customer").hasAuthority(Role.CUSTOMER.name)
                it.requestMatchers(HttpMethod.GET, "/job/professional").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.PATCH, "/job/complete/**", "/job/cancel/**").hasAnyAuthority(Role.CUSTOMER.name, Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.GET, "/job/requestedJobs").hasAuthority(Role.CUSTOMER.name)
                it.requestMatchers(HttpMethod.GET, "/job/offeredJobs").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.GET, "/job/jobOffers").hasAuthority(Role.CUSTOMER.name)
                it.requestMatchers(HttpMethod.POST, "/job/jobOffers").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.DELETE, "/job/jobOffers").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.POST, "/job/acceptJobOffer").hasAuthority(Role.CUSTOMER.name)
                it.requestMatchers(HttpMethod.GET, "/job/jobRequests").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.POST, "/job/requestJob").hasAuthority(Role.CUSTOMER.name)
                it.requestMatchers(HttpMethod.DELETE, "/job/requestJob").hasAuthority(Role.CUSTOMER.name)

                it.requestMatchers(HttpMethod.GET, "/professional/**").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.POST, "/professional/**").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.PATCH, "/professional/**").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.DELETE, "/professional/**").hasAuthority(Role.PROFESSIONAL.name)

                it.requestMatchers(HttpMethod.GET, "/rating/**").hasAnyAuthority(Role.CUSTOMER.name, Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.POST, "/rating/**").hasAnyAuthority(Role.CUSTOMER.name, Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.PATCH, "/rating/**").hasAnyAuthority(Role.CUSTOMER.name, Role.PROFESSIONAL.name)


                it.requestMatchers(HttpMethod.GET, "/user/seeCustomerProfile/**").hasAuthority(Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.GET, "/user/seeProfessionalProfile/**").hasAuthority(Role.CUSTOMER.name)
                it.requestMatchers(HttpMethod.GET, "/user/**").hasAnyAuthority(Role.CUSTOMER.name, Role.PROFESSIONAL.name)
                it.requestMatchers(HttpMethod.PATCH, "/user/**").hasAnyAuthority(Role.CUSTOMER.name, Role.PROFESSIONAL.name)

                it.anyRequest().authenticated()
            }
            .httpBasic(
                Customizer.withDefaults())
            .sessionManagement {
                configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(
                jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling(
                Customizer.withDefaults())
            .build()
    }
}
