package api.users.repository

import api.users.entity.Stack
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StackRepository : CrudRepository<Stack, UUID> {
    @Query(value = "DELETE FROM stacks WHERE user_id = ?1", nativeQuery = true)
    fun deleteStackByUserId(userId: UUID)

    @Query(value = "SELECT * FROM stacks WHERE user_id = ?1", nativeQuery = true)
    fun findStackByUserId(userId: UUID): List<Stack>

}