package api.users.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

//@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(message: String) : RuntimeException(message)
