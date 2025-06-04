package quickfix.services

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import quickfix.dao.AddressRepository
import quickfix.dto.address.AddressDTO
import quickfix.dto.address.toAddress
import quickfix.models.Address
import quickfix.models.User
import quickfix.utils.MAXIMUM_ADDRESSES
import quickfix.utils.exceptions.AddressException
import quickfix.utils.exceptions.NotFoundException

@Service
class AddressService(
    private val addressRepository: AddressRepository
){

    fun getPrimaryAddress(customerId: Long) : Address =
        addressRepository.findPrincipalAddressByUserId(customerId) ?: throw NotFoundException("Ha habido un error al recuperar la dirección")

    fun findSecondaryAddresses(customerId: Long) : List<Address> =
        addressRepository.findSecondaryAddressesByUserId(customerId)

    @Transactional
    fun updatePrimaryAddress(userId: Long, newAddressInfo: AddressDTO){
        val primaryAddress = addressRepository.findPrincipalAddressByUserId(userId) ?: throw NotFoundException("Ha habido un error al recuperar la dirección")
        primaryAddress.updateAddressInfo(newAddressInfo)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun addSecondaryAddress(customer: User, addressDTO: AddressDTO) {
        assertAliasDoesNotExists(customer.id, addressDTO.alias)
        validateMaximumAddresses(customer.id)
        val address = addressDTO.toAddress(customer)
        addressRepository.save(address)
    }

    @Transactional(rollbackFor = [Exception::class])
    fun removeSecondaryAddress(customerId: Long, alias: String) {
        assertAliasExists(customerId, alias)
        val address = addressRepository.findByAlias(alias.trim().replace("\"", "")) ?: throw AddressException("No se ha podido encontrar la dirección que desea eliminar.")
        addressRepository.delete(address)
    }

    private fun assertAliasDoesNotExists(customerId: Long, alias: String?){
        if(alias == null)
            throw AddressException("Debe ingresar un alias.")
        if(addressRepository.existsByUserIdAndAlias(customerId, alias.trim().replace("\"", "")))
            throw AddressException("Ya tiene una dirección secundaria con el mismo alias.")
    }

    private fun assertAliasExists(customerId: Long, alias: String?){
        if(alias == null)
            throw AddressException("Debe ingresar un alias.")
        if(!addressRepository.existsByUserIdAndAlias(customerId, alias.trim().replace("\"", "")))
            throw AddressException("No tiene una dirección secundaria con el alias que intenta eliminar.")
    }

    private fun validateMaximumAddresses(customerId: Long){
        val addressesQuantity = addressRepository.countByUserId(customerId)
        if(addressesQuantity >= MAXIMUM_ADDRESSES)
            throw AddressException("Ha alcanzado el máximo de direcciones secundarias. Por favor, borre alguna para agregar otra.")
    }
}