package quickfix.services

import org.springframework.stereotype.Service
import quickfix.dao.AddressRepository
import quickfix.utils.exceptions.BusinessException

@Service
class AddressService(
    private val addressRepository: AddressRepository
){
    fun getAddressByZipCode(zipcode: String) = addressRepository.findAddressByZipCode(zipcode) ?: throw BusinessException("No se encontró la dirección con ese código postal")
}