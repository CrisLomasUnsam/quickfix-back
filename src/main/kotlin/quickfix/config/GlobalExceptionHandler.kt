package quickfix.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import quickfix.utils.exceptions.*

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalDataException::class)
    fun handleIllegalData(e: IllegalDataException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: NotFoundException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(e: InvalidCredentialsException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
    }

    @ExceptionHandler(RatingException::class)
    fun handleRating(e: RatingException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidToken(e: InvalidTokenException): ResponseEntity<String> {
        return ResponseEntity.status(498).body(e.message)
    }

    @ExceptionHandler(JobException::class)
    fun handleJob(e: JobException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
    }

    @ExceptionHandler(AddressException::class)
    fun handleAddress(e: AddressException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
    }

    @ExceptionHandler(ProfessionalException::class)
    fun handleProfessional(e: ProfessionalException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
    }

}