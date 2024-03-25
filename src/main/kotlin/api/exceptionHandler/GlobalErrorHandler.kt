package api.exceptionHandler

import api.jobs.Exception.JobNotFoundException
import api.users.exception.UserNotFoundException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalErrorHandler {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
    }
    // arrumar os any do retorno
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArguentNotValidException(
        req: HttpServletRequest,
        exception: MethodArgumentNotValidException
    ): ResponseEntity<Any> {

        log.error("MethodArgumentNotValidException observed : ${exception.message}", exception)
        val errors = exception.bindingResult.allErrors
            .map { error -> error.defaultMessage!! }
            .sorted()

        log.info("errors : $errors")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseError(errors.formatErrorMessage()))
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleNotFoundException(ex: UserNotFoundException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseError(mapOf("code" to "user_not_found", "description" to ex.message)))
    }

    @ExceptionHandler(JobNotFoundException::class)
    fun handleNotFoundException(ex: JobNotFoundException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ResponseError(mapOf("code" to "job_not_found", "description" to ex.message)))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotFoundException(ex: HttpMessageNotReadableException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ResponseError(mapOf("code" to "error", "description" to ex.message)))

    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(request: HttpServletRequest, ex: Exception): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseError(mapOf("server_error" to "internal_error", "description" to ex.message)))
    }
}

fun List<String>.formatErrorMessage(): MutableList<Map<String, String>> {
    val err = this.map {
        mapOf("code" to "argument_not_valid", "description" to it)
    }.toMutableList()

    val errorList: MutableList<Map<String, String>> = mutableListOf()

    errorList.addAll(err)

    return errorList
}