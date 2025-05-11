package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.message.MessageDTO
import quickfix.dto.message.MessageResponseDTO
import quickfix.services.JobService

@RestController
@RequestMapping("/chat")
@Tag(name = "Chat", description = "Operaciones relacionadas a la mensajería entre un customer y un professional")
class ChatController (
    private val jobService: JobService
) {

    @ModelAttribute("currentUserId")
    fun getCurrentUserId(): Long {
        val usernamePAT = SecurityContextHolder.getContext().authentication
        return usernamePAT.principal.toString().toLong()
    }

    @GetMapping("/{jobId}")
    fun getMessages(@ModelAttribute("currentUserId") currentUserId: Long, @PathVariable jobId: Long) : List<MessageResponseDTO> =
        jobService.getChatMessages(currentUserId, jobId)

    @PostMapping
    fun postMessage(@ModelAttribute("currentUserId") currentUserId: Long, @RequestBody message: MessageDTO) {
        jobService.postChatMessage(currentUserId, message)
    }

}