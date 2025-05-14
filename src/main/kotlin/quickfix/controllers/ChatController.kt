package quickfix.controllers

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import quickfix.dto.chat.ChatUserInfoDTO
import quickfix.dto.chat.MessageDTO
import quickfix.dto.chat.MessageResponseDTO
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

    @GetMapping("/customer/{jobId}")
    fun getCustomerMessages(@ModelAttribute("currentUserId") currentUserId: Long, @PathVariable jobId: Long) : List<MessageResponseDTO> =
        jobService.getCustomerChatMessages(currentUserId, jobId)

    @GetMapping("/professional/{jobId}")
    fun getProfessionalMessages(@ModelAttribute("currentUserId") currentUserId: Long, @PathVariable jobId: Long) : List<MessageResponseDTO> =
        jobService.getProfessionalChatMessages(currentUserId, jobId)

    @PostMapping("/customer")
    fun postCustomerMessage(@ModelAttribute("currentUserId") currentUserId: Long, @RequestBody message: MessageDTO) {
        jobService.postCustomerChatMessage(currentUserId, message)
    }

    @PostMapping("/professional")
    fun postProfessionalMessage(@ModelAttribute("currentUserId") currentUserId: Long, @RequestBody message: MessageDTO) {
        jobService.postProfessionalChatMessage(currentUserId, message)
    }

    @GetMapping("/professional/chatInfo/{jobId}")
    fun getProfessionalChatInfo(@ModelAttribute("currentUserId") currentUserId: Long, @PathVariable jobId: Long): ChatUserInfoDTO {
        return  ChatUserInfoDTO.toDTO(jobService.getProfessionalChatInfo(currentUserId, jobId))
    }

    @GetMapping("/customer/chatInfo/{jobId}")
    fun getCustomerChatInfo(@ModelAttribute("currentUserId") currentUserId: Long, @PathVariable jobId: Long): ChatUserInfoDTO {
        return ChatUserInfoDTO.toDTO(jobService.getCustomerChatInfo(currentUserId, jobId))
    }
}