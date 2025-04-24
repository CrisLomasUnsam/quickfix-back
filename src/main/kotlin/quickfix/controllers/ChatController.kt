package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import quickfix.dto.message.ChatMessageDTO
import quickfix.dto.message.RedisMessageDTO
import quickfix.services.JobService

@RestController
@RequestMapping("/chat")
@Tag(name = "Chat", description = "Operaciones relacionadas a la mensajería entre un customer y un professional")
class ChatController (
    private val jobService: JobService
) {
    @GetMapping("/{customerId}/{professionalId}/{jobId}")
    fun getMessages(@PathVariable customerId: Long, @PathVariable professionalId: Long, @PathVariable jobId: Long) : List<RedisMessageDTO> =
        jobService.getChatMessages(customerId, professionalId, jobId)

    @PostMapping
    fun postMessage(@RequestBody message: ChatMessageDTO) {
        jobService.postChatMessage(message)
    }
}