package quickfix.dao

import org.springframework.data.repository.CrudRepository
import quickfix.models.Rol
import java.util.*

interface RoleRepository : CrudRepository<Rol, Long> {
    fun findByName(roleName: String): Optional<Rol>
}