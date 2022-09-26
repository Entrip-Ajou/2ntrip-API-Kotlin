package com.entrip.controller

import com.entrip.domain.RestAPIMessages
import com.entrip.domain.dto.Votes.VotesSaveRequestDto
import com.entrip.service.VotesService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset

@RestController
class VotesController(
    val votesService : VotesService
) {
    private fun sendResponseHttpByJson(message: String, data: Any) : ResponseEntity<RestAPIMessages> {
        val restAPIMessages = RestAPIMessages(
            httpStatus = 200,
            message = message,
            data = data
        )
        val headers = HttpHeaders()
        headers.contentType = MediaType("application", "json", Charset.forName("UTF-8"))
        return ResponseEntity<RestAPIMessages>(restAPIMessages, headers, HttpStatus.OK)
    }

    // 투표 작성 리턴 VotesReturnDto
    @PostMapping("/api/v1/votes")
    fun save(@RequestBody requestDto : VotesSaveRequestDto) : ResponseEntity<RestAPIMessages> {
        val voteId : Long = votesService.save(requestDto)!!
        val returnDto = votesService.findById(voteId)
        return sendResponseHttpByJson("Votes is saved well", returnDto)
    }
}