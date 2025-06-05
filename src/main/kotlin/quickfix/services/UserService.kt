package quickfix.services

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import quickfix.dao.TokenRepository
import quickfix.dao.UserRepository
import quickfix.dto.address.AddressDTO
import quickfix.dto.user.ISeeUserProfile
import quickfix.dto.user.UserModifiedInfoDTO
import quickfix.dto.user.UserProfileInfoDto
import quickfix.models.*
import quickfix.utils.FRONTEND_URL
import quickfix.utils.events.OnChangePasswordRequestEvent
import quickfix.utils.events.OnChangedUserInfoEvent
import quickfix.utils.exceptions.NotFoundException
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val tokenRepository: TokenRepository,
    private val imageService: ImageService,
    private val addressService: AddressService
) {

    private fun createRecoveryURL(token: String) = "$FRONTEND_URL/newPassword?token=$token"

    fun getById(id: Long): User =
        userRepository.findById(id).orElseThrow{ NotFoundException("Usuario no encontrado $id") }

    fun getUserProfileInfo(userId: Long) : UserProfileInfoDto {
        val user = userRepository.findById(userId).orElseThrow { NotFoundException("Usuario no encontrado.") }
        val address = addressService.getPrimaryAddress(userId)
        return UserProfileInfoDto.toDTO(user, address)
    }

    fun findByMail(mail : String) : Optional<User> =
        userRepository.findByMail(mail)

    fun getByMail(mail: String): User =
        userRepository.findByMail(mail).orElseThrow{ NotFoundException("Usuario no encontrado $mail") }

    fun save(user : User) = userRepository.save(user)

    fun assertUserExists(id: Long) {
        if (!userRepository.existsById(id))
            throw NotFoundException("Usuario no encontrado: $id")
    }

    fun userHasRatedJob(userId: Long, jobId: Long) : Boolean =
        userRepository.userHasRatedJob(userId, jobId)

    fun getProfessionalInfo(userId: Long) : ProfessionalInfo =
        userRepository.findUserWithProfessionalInfoById(userId).orElseThrow{ NotFoundException() }.professionalInfo

    fun getActiveProfessionsByUserId(id: Long): Set<Profession> =
        getProfessionalInfo(id).getActiveProfessions()

    fun getSeeProfessionalProfileInfo(professionalId : Long) : ISeeUserProfile =
        userRepository.getSeeProfessionalProfileInfo(professionalId)

    fun getSeeCustomerProfileInfo(customerId : Long) : ISeeUserProfile =
        userRepository.getSeeCustomerProfileInfo(customerId)

    @Transactional(rollbackFor = [Exception::class])
    fun changeUserInfo(userId: Long, modifiedInfo: UserModifiedInfoDTO) {
        val user = this.getById(userId)
        user.updateUserInfo(modifiedInfo)
        val newAddressInfo = AddressDTO.fromUserModifiedInfoDTO(modifiedInfo)
        addressService.updatePrimaryAddress(userId, newAddressInfo)
        eventPublisher.publishEvent(OnChangedUserInfoEvent(user.mail))
    }

    fun getSecondaryAddress(customerId: Long) : List<Address> = addressService.findSecondaryAddresses(customerId)

    @Transactional(rollbackFor = [Exception::class])
    fun addSecondaryAddress(customerId: Long, address: AddressDTO) {
        val customer = getById(customerId)
        addressService.addSecondaryAddress(customer, address)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun removeSecondaryAddress(customerId: Long, addressAlias: String) =
        addressService.removeSecondaryAddress(customerId, addressAlias )

    @Transactional(rollbackFor = [Exception::class])
    fun requestChangePassword(mail: String) {
        val user = getByMail(mail)
        val token = tokenRepository.save(Token.createTokenEntity(user))
        val recoveryURL = createRecoveryURL(token.value)
        eventPublisher.publishEvent(OnChangePasswordRequestEvent(user, recoveryURL))

    }

    @Transactional(rollbackFor = [Exception::class])
    fun updateAvatar(currentUserId: Long, file: MultipartFile) {
        imageService.uploadProfileImage(currentUserId, file)
    }

}