package com.joaovq.appsystem.exception

import org.springframework.dao.DataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodValidArgumentException(ex: MethodArgumentNotValidException): ResponseEntity<ExceptionDetails> {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.forEach { objectError ->
            val fieldName: String = (objectError as FieldError).field
            val messageError: String? = objectError.defaultMessage
            errors[fieldName] = messageError
        }
        return ResponseEntity.status(ex.statusCode).body(
            ExceptionDetails(
                "Bad request! Consult documentation",
                LocalDateTime.now(),
                ex.statusCode.value(),
                ex::class.toString(),
                errors
            )
        )
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(ex: DataAccessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ExceptionDetails(
                "Conflict! Consult documentation",
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex::class.toString(),
                mutableMapOf(ex.cause.toString() to ex.message)
            )
        )
    }

    private fun getBadRequestResponse(ex: Exception) = ExceptionDetails(
        "Bad request! Consult documentation",
        LocalDateTime.now(),
        HttpStatus.BAD_REQUEST.value(),
        ex::class.toString(),
        mutableMapOf(ex.cause.toString() to ex.message)
    )

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            getBadRequestResponse(ex)
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ExceptionDetails> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            getBadRequestResponse(ex)
        )
    }
}