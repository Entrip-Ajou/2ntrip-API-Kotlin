package com.entrip.exception

import com.entrip.domain.Messages
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.nio.charset.Charset

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException (e : MethodArgumentNotValidException) : ResponseEntity<Messages> {
        val messages : Messages = Messages(
            400,
            "MethodArgumentNotValidException" + e.message,
            null
        )
        val headers : HttpHeaders = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        return ResponseEntity<Messages>(messages, headers, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException (e : HttpRequestMethodNotSupportedException) : ResponseEntity<Messages> {
        val messages : Messages = Messages(
            405,
            "HttpRequestMethodNotSupportedException" + e.message,
            null
        )
        val headers : HttpHeaders = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        return ResponseEntity<Messages>(messages, headers, HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException (e : IllegalArgumentException) : ResponseEntity<Messages> {
        val messages : Messages = Messages(
            500,
            "IllegalArgumentException" + e.message,
            null
        )
        val headers : HttpHeaders = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        return ResponseEntity<Messages>(messages, headers, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NicknameOrUserIdNotValidException::class)
    fun handleNicknameOrUserIdNotValidException (e : NicknameOrUserIdNotValidException) : ResponseEntity<Messages> {
        val messages : Messages = Messages(
            404,
            "NicknameOrUserIdNotValidException" + e.message,
            null
        )
        val headers : HttpHeaders = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        return ResponseEntity<Messages>(messages, headers, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(Exception::class)
    fun handleException (e : Exception) : ResponseEntity<Messages> {
        val messages : Messages = Messages(
            520,
            "handleEntityNotFoundException : unknown exception\n" + e.message,
            null
        )
        val headers : HttpHeaders = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        return ResponseEntity<Messages>(messages, headers, HttpStatus.SERVICE_UNAVAILABLE)
    }
}