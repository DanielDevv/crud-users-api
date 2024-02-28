package api.users.service

import api.users.dto.PageDTO
import api.users.dto.Pagination
import api.users.dto.StackDTO
import api.users.dto.UserDTO
import api.users.entity.Stack
import api.users.entity.User
import api.users.exception.UserNotFoundException
import api.users.repository.StackRepository
import api.users.repository.UserRepository
import mu.KLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import java.util.*

@Service
class UserService(val userRepository: UserRepository, val stackRepository: StackRepository) {

    companion object : KLogging()

    fun createUser(user: UserDTO): UserDTO {

        val userEntity = User(null, user.nickname, user.name, user.birthDate)

        val saveUser = userRepository.save(userEntity)

        val stack = user.stack.map {

            Stack(null, it.name, it.score, saveUser)
        }.toSet()

        stackRepository.saveAll(stack)

        return UserDTO(userEntity.id, user.nickname, user.name, user.birthDate, user.stack)
    }

    fun getAllUsers(page: Int, size: Int): Pagination {

        return userRepository.findAll(
            PageRequest.of(
                page,
                size, Sort.by(Sort.Direction.DESC, "name")
            )
        )
            .let {

                val users = it.map {
                    val stack = it.stack.map { StackDTO(it.name, it.score) }.toSet()

                    UserDTO(it.id, it.nickname, it.name, it.birthDate, stack)
                }

                val totalElements = userRepository.count().toInt()

                return Pagination(PageDTO(records = users.toList(), page, size, total = totalElements), it.hasNext())
            }
    }

    fun updateUser(userId: UUID, user: UserDTO): UserDTO {

        val userExists =
            userRepository.findById(userId)
                .orElseThrow { UserNotFoundException("No user found for the passed in Id: $userId") }

        return userExists.let {
            val userEntity = it.copy()

            val stack = user.stack.map { st -> Stack(null, st.name, st.score, userEntity) }.toSet()

            userEntity.nickname = user.nickname
            userEntity.name = user.name
            userEntity.birthDate = user.birthDate
            userEntity.stack = stack

            userRepository.save(userEntity)

            userEntity.stack.let {
                stackRepository.saveAll(it)
            }

            UserDTO(userId, it.nickname, it.name, it.birthDate, user.stack)
        }

    }

    fun deleteUser(userId: UUID) {
        userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("No user found for the passed in Id: $userId") }

        userRepository.deleteById(userId)
        stackRepository.deleteStackByUserId(userId)
    }

    fun getUser(userId: UUID): UserDTO {

        val userExists = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("No user found for the passed in Id: $userId") }

        val stack = userExists.stack.map { StackDTO(it.name, it.score) }.toSet()

        println("list stack getUser: $stack")

        return UserDTO(userExists.id, userExists.nickname, userExists.name, userExists.birthDate, stack)
    }

    fun getStacks(userId: UUID): List<StackDTO> {
        userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("No user found for the passed in Id: $userId") }

        val findStack = stackRepository.findStackByUserId(userId)

        return findStack.map { st -> StackDTO(st.name, st.score) }
    }
}

