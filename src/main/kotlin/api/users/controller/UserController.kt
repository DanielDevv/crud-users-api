package api.users.controller

import api.users.dto.UserDTO
import api.users.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/users")
@Validated
class UserController(val userService: UserService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody @Valid user: UserDTO): UserDTO {
        return userService.createUser(user)
    }

    @GetMapping
    fun getAllUsers(): List<UserDTO> = userService.getAllUsers()

    @GetMapping("/{user_id}")
    fun getUser(@PathVariable("user_id") userId: UUID): UserDTO = userService.getUser(userId)

    @PutMapping("/{user_id}")
    fun updateUser(@RequestBody @Valid user: UserDTO, @PathVariable("user_id") userId: UUID): UserDTO = userService.updateUser(userId, user)

    @DeleteMapping("/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("user_id") userId: UUID) = userService.deleteUser(userId)
}