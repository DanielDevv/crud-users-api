package api.users.exceptionHandler


data class ResponseError<T>(
    val error_message: T
)