package quickfix.controllers

import org.springframework.web.bind.annotation.*
import quickfix.dto.message.ChatMessageDTO
import quickfix.dto.message.RedisMessageDTO
import quickfix.services.JobService

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = ["*"])

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