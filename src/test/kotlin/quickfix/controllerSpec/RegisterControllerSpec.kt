package quickfix.controllerSpec

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.annotation.DisplayName
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
import quickfix.dao.UserRepository
import quickfix.dto.register.RegisterRequestDTO
import quickfix.models.Gender

@SpringBootTest(
    classes = [QuickFixApp::class],
    properties = ["spring.profiles.active=test"]
)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Login controller test (use DataInitializer info)")
class RegisterControllerSpec {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeAll
    fun init() {
        userRepository.save(CustomerBuilder.buildMock("mailExist"))
    }

    @Test
    fun `Create a user`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "valentino@gmail.com",
                    "Name",
                    "Lastname",
                    "password",
                    12345678,
                    "01/01/2000",
                    Gender.OTHER,
                    "StreetAddress1",
                    "StreetAddress2",
                    "1234",
                    "City",
                    "State"
                )
            )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerData)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    fun `Try create a user with a existing mail`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mailExist@gmail.com",
                    "",
                    "",
                    "",
                    1,
                    "",
                    Gender.OTHER,
                    "",
                    "",
                    "",
                    "",
                    ""
                )
            )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerData)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("El usuario con mail mailexist@gmail.com ya existe"))
    }

    @Test
    fun `Try create a user with a wrong dateBirth format`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail@gmail.com",
                    "Name",
                    "Lastname",
                    "password",
                    12345678,
                    "",
                    Gender.OTHER,
                    "StreetAddress1",
                    "StreetAddress2",
                    "1234",
                    "City",
                    "State"
                )
            )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerData)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("La fecha no tiene el formato esperado dd/MM/yyyy"))
    }
}