package api.users.repository

import api.users.dto.StackDTO
import api.users.entity.Stack
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface StackRepository : CrudRepository<Stack, UUID> {
    @Query(value = "DELETE FROM stacks WHERE user_id = ?1", nativeQuery = true)
    fun deleteStackByUserId(userId: UUID)

    @Query(value = "SELECT * FROM stacks WHERE user_id = ?1", nativeQuery = true)
    fun findStackByUserId(userId: UUID): List<Stack>

}