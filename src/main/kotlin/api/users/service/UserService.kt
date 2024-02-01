package api.users.service

import api.users.dto.UserDTO
import api.users.entity.User
import api.users.exception.UserNotFoundException
import api.users.repository.UserRepository
import mu.KLogging
import org.hibernate.validator.constraints.UUID
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    companion object : KLogging()

    fun createUser(user: UserDTO): UserDTO {

        val userEntity = user.let {
            User(null, it.nickname ?: "", it.name, it.birth_date, it.stack)
        }

        userRepository.save(userEntity)

        logger.info("Saved user is: $userEntity")

        return userEntity.let {
            UserDTO(it.id, it.nickname, it.name, it.birth_date, it.stack)
        }

    }

    fun getAllUsers(): List<UserDTO> {
        return userRepository.findAll()
            .map {
                UserDTO(it.id, it.nickname, it.name, it.birth_date, it.stack)
            }
    }

    fun updateUser(userId: java.util.UUID, user: UserDTO): UserDTO {

        val userExists = userRepository.findById(userId)

        if(!userExists.isPresent) {
            logger.info("User not found!")
            throw UserNotFoundException("No user found for the passed in Id: $userId")
        }

        return userExists.get().let {
            val userEntity = it.copy()

            userEntity.nickname = user.nickname
            userEntity.name = user.name
            userEntity.birth_date = user.birth_date
            userEntity.stack = user.stack
            userRepository.save(userEntity)

            UserDTO(it.id, it.nickname, it.name, it.birth_date, it.stack)
            }
    }

    fun deleteUser(userId: java.util.UUID) {
        val userExists = userRepository.findById(userId)

        if(!userExists.isPresent) {
            logger.info("User not found!")
            throw UserNotFoundException("No user found for the passed in Id: $userId")
        }

        userExists.get()
            .let {
                userRepository.deleteById(userId)
            }
    }

    fun getUser(userId: java.util.UUID): UserDTO {

        val userExists = userRepository.findById(userId)

        if(!userExists.isPresent) {
            logger.info("User not found!")
            throw UserNotFoundException("No user found for the passed in Id: $userId")
        }

        return userExists.get()
            .let {
                UserDTO(it.id, it.nickname, it.name, it.birth_date, it.stack)
        }
    }

}
