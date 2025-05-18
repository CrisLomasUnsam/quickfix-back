package quickfix.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import quickfix.utils.exceptions.*

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalDataException::class)
    fun handleIllegalData(e: IllegalDataException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(e: NotFoundException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(InvalidCredentialsException::class)
    fun handleInvalidCredentials(e: InvalidCredentialsException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(RatingException::class)
    fun handleRating(e: RatingException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(InvalidTokenException::class)
    fun handleInvalidToken(e: InvalidTokenException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(498).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(JobException::class)
    fun handleJob(e: JobException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(AddressException::class)
    fun handleAddress(e: AddressException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(ProfessionalException::class)
    fun handleProfessional(e: ProfessionalException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse(e.message))
    }

    @ExceptionHandler(ImageException::class)
    fun handleImage(e: ImageException): ResponseEntity<SimpleResponse> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse(e.message))
    }

}