package quickfix.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import quickfix.models.Address

@Component
interface AddressRepository  : CrudRepository<Address, Long>{
    fun findAddressByZipCode(zipcode: String): Address?
}