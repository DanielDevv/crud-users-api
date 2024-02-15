package api.users.service

import api.users.dto.PageDTO
import api.users.dto.StackDTO
import api.users.dto.UserDTO
import api.users.entity.Stack
import api.users.entity.User
import api.users.exception.StackNameDuplicatedException
import api.users.exception.UserNotFoundException
import api.users.repository.StackRepository
import api.users.repository.UserRepository
import mu.KLogging
import org.apache.coyote.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userRepository: UserRepository, val stackRepository: StackRepository) {

    companion object : KLogging()

    fun createUser(user: UserDTO): UserDTO {

        val userEntity = User(null, user.nickname, user.name, user.birth_date)

        if(user.stack?.validateStackNameDuplicated()!!) throw StackNameDuplicatedException("stack name repeated")

       val saveUser = userRepository.save(userEntity)

        val stack = user.stack?.map {

            Stack(null, it.name, it.score, saveUser)
        }

        stack?.let {
            logger.info("stack: $stack")

            stackRepository.saveAll(stack)
        }

        return UserDTO(userEntity.id, user.nickname, user.name, user.birth_date, user.stack)
    }

    fun getAllUsers(page: Int, size: Int): ResponseEntity<PageDTO> {

        return userRepository.findAll(
            PageRequest.of(
                page,
                size, Sort.by(Sort.Direction.DESC, "name")))
            .let {

                val users = it.map {
                    val stack = it.stack?.map { StackDTO(it.name, it.score) }

                    UserDTO(it.id, it.nickname, it.name, it.birth_date, stack)
                }

                val totalElements = userRepository.count().toInt()

                var status: HttpStatus = HttpStatus.OK

                if(it.hasNext()) {
                    status = HttpStatus.PARTIAL_CONTENT
                }

                ResponseEntity(PageDTO(records = users.toList(), page, size, total = totalElements), status)
            }
    }

    fun updateUser(userId: UUID, user: UserDTO): UserDTO {

        val userExists = userRepository.findById(userId)

        if(!userExists.isPresent) {
            logger.info("User not found!")
            throw UserNotFoundException("No user found for the passed in Id: $userId")
        }

        if(user.stack?.validateStackNameDuplicated()!!) throw StackNameDuplicatedException("stack name repeated")

        return userExists.get().let {
            val userEntity = it.copy()

            val stack = user.stack?.map { st -> Stack(null, st.name, st.score, userEntity) }

            userEntity.nickname = user.nickname
            userEntity.name = user.name
            userEntity.birth_date = user.birth_date
            userEntity.stack = stack

            userRepository.save(userEntity)

            userEntity.stack?.let {
                stackRepository.saveAll(it)
            }

            UserDTO(userId, it.nickname, it.name, it.birth_date, user.stack)
            }
    }

    fun deleteUser(userId: UUID) {
        userRepository.findById(userId).orElseThrow { UserNotFoundException("No user found for the passed in Id: $userId") }

        userRepository.deleteById(userId)
        stackRepository.deleteStackByUserId(userId)
    }

    fun getUser(userId: UUID): UserDTO {

        val userExists = userRepository.findById(userId).orElseThrow { UserNotFoundException("No user found for the passed in Id: $userId") }

        val stack = userExists.stack?.map { StackDTO(it.name, it.score) }

        return UserDTO(userExists.id, userExists.nickname, userExists.name, userExists.birth_date, stack)
    }

    fun getStacks(userId: UUID): List<StackDTO> {
        userRepository.findById(userId).orElseThrow { UserNotFoundException("No user found for the passed in Id: $userId") }

        val findStack = stackRepository.findStackByUserId(userId)

        return findStack.map { st -> StackDTO(st.name, st.score) }
    }
}

fun List<StackDTO>.validateStackNameDuplicated() : Boolean {
    val stackNames = this.map { st -> st.name }

    val duplicatedName = stackNames.toMutableSet()

    return duplicatedName.size < stackNames.size
}
