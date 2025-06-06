package quickfix.controllerSpec

import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.startsWith
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import quickfix.QuickFixApp
import quickfix.bootstrap.builders.CustomerBuilder
import quickfix.dao.AddressRepository
import quickfix.dao.UserRepository
import quickfix.dto.login.LoginDTO

@SpringBootTest(
    classes = [QuickFixApp::class],
    properties = ["spring.profiles.active=test"]
)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerSpec {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @BeforeAll
    fun init() {
        this.final()
        userRepository.save(CustomerBuilder.buildMock("Valentino"))
    }

    @AfterAll
    fun final() {
        addressRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `Login with valen as customer`() {

        val loginCustomerData = objectMapper.writeValueAsString(LoginDTO("valentino@gmail.com", "password"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/login/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCustomerData)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(startsWith("eyJ")))
    }

    @Test
    fun `Try login with valen as customer (wrong password)`() {

        val loginCustomerData = objectMapper.writeValueAsString(LoginDTO("valentino@gmail.com", "wrongPassword"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/login/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCustomerData)
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
    }

    @Test
    fun `Try login with nonexistent mail as customer`() {

        val loginCustomerData = objectMapper.writeValueAsString(LoginDTO("nonexistentMail@gmail.com", "password"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/login/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCustomerData)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Usuario no encontrado nonexistentMail@gmail.com"))
    }

    @Test
    fun `Login with valen as professional`() {

        val loginCustomerData = objectMapper.writeValueAsString(LoginDTO("valentino@gmail.com", "password"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/login/professional")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCustomerData)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(startsWith("eyJ")))
    }

    @Test
    fun `Try login with valen as professional (wrong password)`() {

        val loginCustomerData = objectMapper.writeValueAsString(LoginDTO("valentino@gmail.com", "wrongPassword"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/login/professional")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCustomerData)
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
    }

    @Test
    fun `Try login with nonexistent mail as professional`() {

        val loginCustomerData = objectMapper.writeValueAsString(LoginDTO("nonexistentMail@gmail.com", "password"))

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/login/professional")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginCustomerData)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Usuario no encontrado nonexistentMail@gmail.com"))
    }
}