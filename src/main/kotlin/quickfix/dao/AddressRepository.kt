package quickfix.dao

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Component
import quickfix.models.Address

@Component
interface AddressRepository  : CrudRepository<Address, Long>{

    @Query("select * from addresses where user_id = :userId and principal = true", nativeQuery = true)
    fun findPrincipalAddressByUserId(@Param("userId") userId: Long): Address?

    @Query("select * from addresses where user_id = :userId and principal = false order by alias asc", nativeQuery = true)
    fun findSecondaryAddressesByUserId(@Param("userId") userId: Long): List<Address>

    @Query("select * from addresses where user_id = :userId order by principal desc, alias asc", nativeQuery = true)
    fun findAllByUserId(@Param("userId") userId: Long): List<Address>

    fun countByUserId(@Param("userId") userId: Long): Long

    fun existsByUserIdAndAlias(@Param("userId") userId: Long, @Param("alias") alias: String): Boolean

    fun findByAlias(@Param("alias") alias: String): Address?

}