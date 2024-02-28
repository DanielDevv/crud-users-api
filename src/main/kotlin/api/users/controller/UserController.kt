package api.users.controller

import api.users.dto.Pagination
import api.users.dto.StackDTO
import api.users.dto.UserDTO
import api.users.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

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
    fun getAllUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "15") size: Int
    ): ResponseEntity<Pagination> {
        val users = userService.getAllUsers(page, size)

        if (users.hasNext) {
            return ResponseEntity.status(206).body(users)
        }

        return ResponseEntity.status(200).body(users)
    }

    @GetMapping("/{user_id}")
    fun getUser(@PathVariable("user_id") userId: UUID): UserDTO = userService.getUser(userId)

    @PutMapping("/{user_id}")
    fun updateUser(@RequestBody @Valid user: UserDTO, @PathVariable("user_id") userId: UUID): Any =
        userService.updateUser(userId, user)

    @DeleteMapping("/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable("user_id") userId: UUID) = userService.deleteUser(userId)

    @GetMapping("/{user_id}/stacks")
    fun getAllStackByUser(@PathVariable("user_id") userId: UUID): List<StackDTO> = userService.getStacks(userId)
}