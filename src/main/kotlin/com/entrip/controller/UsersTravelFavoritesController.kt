package com.entrip.controller

import com.entrip.domain.RestAPIMessages
import com.entrip.domain.entity.TravelFavorite
import com.entrip.domain.entity.UsersTravelFavorites
import com.entrip.repository.UsersTravelFavoritesRepository
import com.entrip.service.UsersTravelFavoritesService
import com.fasterxml.jackson.databind.ObjectMapper
import jdk.nashorn.internal.objects.NativeDebug.getClass
import org.springframework.core.io.ClassPathResource
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.xml.ws.Response
import kotlin.io.path.Path

@RestController
class UsersTravelFavoritesController(
    //private final val usersTravelFavoriteRepository: UsersTravelFavoritesRepository,
    private final val objectMapper: ObjectMapper,
    private final val usersTravelFavoritesService: UsersTravelFavoritesService
) {

    private fun sendResponseHttpByJson(message: String, data: Any): ResponseEntity<RestAPIMessages> {
        val restAPIMessages = RestAPIMessages(
            httpStatus = 200,
            message = message,
            data = data
        )
        val headers = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        return ResponseEntity<RestAPIMessages>(restAPIMessages, headers, HttpStatus.OK)
    }

    @PostMapping("/api/v2/usersTravelFavorite/{user_id}")
    fun addUsersTravelFavorite(
        @PathVariable user_id: String,
        @RequestBody travelFavorite: TravelFavorite
    ): ResponseEntity<RestAPIMessages> =
        sendResponseHttpByJson(
            "add $user_id 's travel favorite",
            usersTravelFavoritesService.addUsersTravelFavorite(user_id, travelFavorite)
        )

    @GetMapping("/api/v2/usersTravelFavorite/all")
    fun getAllUsersTravelFavorite(): ResponseEntity<Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        //return ResponseEntity<Any>(usersTravelFavoritesService.getAllUsersTravelFavorite(), headers, HttpStatus.OK)
        //val paths : Path("/Users/donghwan/Downloads/data.txt")
        val ec2Data: String = "/home/ec2-user/app/step1/entrip-api-kotlin/src/main/resources/data.txt"
        val bytes =
            Files.readAllBytes(Paths.get("/home/ec2-user/app/step1/entrip-api-kotlin/src/main/resources/data.txt"))
        return ResponseEntity<Any>(String(bytes), headers, HttpStatus.OK)
    }


}