package quickfix.controllerSpec

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.InternalPlatformDsl.toStr
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
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
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.register.RegisterRequestDTO
import quickfix.models.Gender
import quickfix.models.User.Companion.EDAD_REQUERIDA
import quickfix.utils.functions.DateWithDayFormatter
import java.time.LocalDate

@SpringBootTest(
    classes = [QuickFixApp::class],
    properties = ["spring.profiles.active=test"]
)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterControllerSpec {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @BeforeEach
    fun init() {
        this.final()
        userRepository.save(CustomerBuilder.buildMock("mailExist"))
    }

    @AfterAll
    fun final() {
        addressRepository.deleteAll()
        tokenRepository.deleteAll()
        userRepository.deleteAll()
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
                    null,
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
    fun `Create a user and confirm token`() {

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

        val token = tokenRepository.getTokensByUser(userRepository.findByMail("valentino@gmail.com").get())

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/registration/confirm")
                .param("token", token.value)
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
    fun `Try create a user with a wrong mail (no contains @)`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail.gmail.com",
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
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("El email no es válido."))
    }

    @Test
    fun `Try create a user with a wrong name (is empty)`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail@gmail.com",
                    "",
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
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("El nombre no puede estar vacío ni contener caracteres especiales o numéricos."))
    }

    @Test
    fun `Try create a user with a wrong lastname (is a number)`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail@gmail.com",
                    "Name",
                    "1",
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
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("El apellido no puede estar vacío ni contener caracteres especiales o numéricos."))
    }

    @Test
    fun `Try create a user with a wrong password (has a blank space)`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail@gmail.com",
                    "Name",
                    "Lastname",
                    "password password",
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
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
    }

    @Test
    fun `Try create a user with a wrong password (length minor 6 chars)`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail@gmail.com",
                    "Name",
                    "Lastname",
                    "passw",
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
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
    }

    @Test
    fun `Try create a user with a wrong dni (length minor 7 number)`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail@gmail.com",
                    "Name",
                    "Lastname",
                    "password",
                    1,
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
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("El DNI es incorrecto."))
    }

    @Test
    fun `Try create a user with a wrong date birth (+18)`() {

        val registerData =
            objectMapper.writeValueAsString(
                RegisterRequestDTO(
                    "mail@gmail.com",
                    "Name",
                    "Lastname",
                    "password",
                    12345678,
                    LocalDate.now().format(DateWithDayFormatter),
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
            .andExpect(jsonPath("$.message").value("El usuario debe ser mayor a $EDAD_REQUERIDA años."))
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
                    LocalDate.now().toStr(),
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

    @Test
    fun `Try Confirm registration non existing token`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/registration/confirm")
                .param("token", "nonExistingToken")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
    }

    @Test
    fun `Try Confirm registration empty token`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/registration/confirm")
                .param("token", "   ")
        )
            .andExpect(status().isUnauthorized)
            .andExpect(jsonPath("$.message").value("Token inválido"))
    }
}