package com.entrip.domain.dto.Votes

class VotesFullInfoReturnDto(
    val title : String,
    val contentsAndUsers : MutableList<UsersAndContentsReturnDto>,
    val multipleVotes: Boolean,
    val anonymousVote: Boolean,
    val host_id : String,
    val voting : Boolean
) {
}