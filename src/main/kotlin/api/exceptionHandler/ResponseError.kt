package api.exceptionHandler


data class ResponseError<T>(
    val error_message: T
)