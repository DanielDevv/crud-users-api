package api.users.exception

import org.springframework.core.NestedRuntimeException

class StackNameDuplicatedException(message: String) : NestedRuntimeException(message)
