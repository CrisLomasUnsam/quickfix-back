package quickfix.controllerSpec

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
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
import quickfix.bootstrap.builders.JobBuilder
import quickfix.bootstrap.builders.ProfessionBuilder
import quickfix.bootstrap.builders.ProfessionalBuilder
import quickfix.dao.AddressRepository
import quickfix.dao.JobRepository
import quickfix.dao.ProfessionRepository
import quickfix.dao.UserRepository
import quickfix.dto.chat.MessageDTO
import quickfix.models.Job
import quickfix.models.Role
import quickfix.models.User
import quickfix.security.JwtTokenUtils
import quickfix.services.RedisService
import quickfix.utils.functions.getAvatarUrl
import quickfix.utils.functions.stringifyDateTimeWithoutYear
import java.time.LocalDateTime

@SpringBootTest(
    classes = [QuickFixApp::class],
    properties = ["spring.profiles.active=test"]
)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatControllerSpec {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var tokenUtils: JwtTokenUtils

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jobRepository: JobRepository

    @Autowired
    private lateinit var redisService: RedisService

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var professionRepository: ProfessionRepository

    private lateinit var valen: User
    private lateinit var cris: User
    private lateinit var tokenValenCustomer: String
    private lateinit var tokenValenProfessional: String
    private lateinit var tokenCrisCustomer: String
    private lateinit var tokenCrisProfessional: String
    private lateinit var job: Job

    @BeforeAll
    fun start() {
        this.clean()

        val professions = professionRepository.saveAll(ProfessionBuilder.buildProfessions())

        valen = userRepository.save(ProfessionalBuilder.buildMock("Valentino", professions = professions))
        tokenValenCustomer = tokenUtils.createToken(valen.id, Role.CUSTOMER.roleName)!!
        tokenValenProfessional = tokenUtils.createToken(valen.id, Role.PROFESSIONAL.roleName)!!

        cris = userRepository.save(ProfessionalBuilder.buildMock("Cristian", professions = professions))
        tokenCrisCustomer = tokenUtils.createToken(cris.id, Role.CUSTOMER.roleName)!!
        tokenCrisProfessional = tokenUtils.createToken(cris.id, Role.PROFESSIONAL.roleName)!!

        job = jobRepository.save(JobBuilder.buildMock(valen, cris, professions.first(), false))
    }

    @BeforeEach
    fun init() {
        redisService.deleteChatMessages(job.id)
    }

    @AfterAll
    fun clean() {
        jobRepository.deleteAll()
        addressRepository.deleteAll()
        userRepository.deleteAll()
        professionRepository.deleteAll()
    }

    @Test
    fun `Get message by professional`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/professional/${job.id}")
                .header("Authorization", "Bearer $tokenCrisProfessional")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `Get message by customer`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/customer/${job.id}")
                .header("Authorization", "Bearer $tokenValenCustomer")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `Post messages and get it by professional and by customer`() {

        val dateTime = stringifyDateTimeWithoutYear(LocalDateTime.now())

        val messageProfessional = objectMapper.writeValueAsString(
            MessageDTO(
                job.id,
                "Message of professional"
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/chat/professional")
                .header("Authorization", "Bearer $tokenCrisProfessional")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageProfessional)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))

        val messageCustomer = objectMapper.writeValueAsString(
            MessageDTO(
                job.id,
                "Message of customer"
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/chat/customer")
                .header("Authorization", "Bearer $tokenValenCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(messageCustomer)
        )
            .andExpect(status().isOk)
            .andExpect(content().string(""))


        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/customer/${job.id}")
                .header("Authorization", "Bearer $tokenValenCustomer")
        )
            .andExpect(status().isOk)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].message").value("Message of professional"))
            .andExpect(jsonPath("$[0].itsMine").value(false))
            .andExpect(jsonPath("$[0].datetime").value(dateTime))
            .andExpect(jsonPath("$[1].message").value("Message of customer"))
            .andExpect(jsonPath("$[1].itsMine").value(true))
            .andExpect(jsonPath("$[1].datetime").value(dateTime))

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/professional/${job.id}")
                .header("Authorization", "Bearer $tokenCrisProfessional")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].message").value("Message of professional"))
            .andExpect(jsonPath("$[0].itsMine").value(true))
            .andExpect(jsonPath("$[0].datetime").value(dateTime))
            .andExpect(jsonPath("$[1].message").value("Message of customer"))
            .andExpect(jsonPath("$[1].itsMine").value(false))
            .andExpect(jsonPath("$[1].datetime").value(dateTime))
    }

    @Test
    fun `Try get message by professional (not have that job)`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/professional/${job.id}")
                .header("Authorization", "Bearer $tokenValenProfessional")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value( "Ha habido un error al obtener los mensajes."))
    }

    @Test
    fun `Try get message by user (not have that job)`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/customer/${job.id}")
                .header("Authorization", "Bearer $tokenCrisCustomer")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value( "Ha habido un error al obtener los mensajes."))
    }

    @Test
    fun `Try post a message by professional (not have that job)`() {

        val message = objectMapper.writeValueAsString(
            MessageDTO(
                job.id,
                "message"
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/chat/professional")
                .header("Authorization", "Bearer $tokenValenProfessional")
                .contentType(MediaType.APPLICATION_JSON)
                .content(message)
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value( "Ha habido un error al enviar el mensaje."))
    }

    @Test
    fun `Try post a message by user (not have that job)`() {

        val message = objectMapper.writeValueAsString(
            MessageDTO(
                job.id,
                "message"
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/chat/customer")
                .header("Authorization", "Bearer $tokenCrisCustomer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(message)
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value( "Ha habido un error al enviar el mensaje."))
    }

    @Test
    fun `Get chat info by professional`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/professional/chatInfo/${job.id}")
                .header("Authorization", "Bearer $tokenCrisProfessional")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.name").value("Valentino"))
            .andExpect(jsonPath("$.lastName").value("Test"))
            .andExpect(jsonPath("$.avatar").value(getAvatarUrl(valen.id)))
    }

    @Test
    fun `Get chat info by customer`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/customer/chatInfo/${job.id}")
                .header("Authorization", "Bearer $tokenValenCustomer")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$.name").value("Cristian"))
            .andExpect(jsonPath("$.lastName").value("Test"))
            .andExpect(jsonPath("$.avatar").value(getAvatarUrl(cris.id)))
    }

    @Test
    fun `Try Get chat info by professional (not have that job)`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/professional/chatInfo/${job.id}")
                .header("Authorization", "Bearer $tokenValenProfessional")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value( "Ha habido un error al obtener los datos solicitados."))
    }

    @Test
    fun `Try Get chat info by customer (not have that job)`() {

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/chat/customer/chatInfo/${job.id}")
                .header("Authorization", "Bearer $tokenCrisCustomer")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value( "Ha habido un error al obtener los datos solicitados."))
    }
}