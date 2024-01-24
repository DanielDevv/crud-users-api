package api.users.exceptionHandler

import api.users.exception.UserNotFoundException
import jakarta.servlet.http.HttpServletRequest
import mu.KLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.format.DateTimeParseException

@ControllerAdvice
class GlobalErrorHandler {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
    }

//    fun handleMethodArgumentNotValid(
//        ex: MethodArgumentNotValidException,
//        headers: HttpHeaders,
//        status: HttpStatusCode,
//        request: WebRequest
//    ): ResponseEntity<Any>? {
//
//        log.error("MethodArgumentNotValidException observed : ${ex.message}", ex)
//        val errors = ex.bindingResult.allErrors
//            .map { error -> error.defaultMessage!! }
//            .sorted()
//
//        log.info("errors : $errors")
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//            .body(errors)
//    }

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
            .body(errors)
    }


    @ExceptionHandler(UserNotFoundException::class)
    fun handleNotFoundException(ex: UserNotFoundException, request: WebRequest) : ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.message)

    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotFoundException(ex: HttpMessageNotReadableException, request: WebRequest) : ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ex.message)

    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(request: HttpServletRequest, ex: Exception) : ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ex.message)

    }
}