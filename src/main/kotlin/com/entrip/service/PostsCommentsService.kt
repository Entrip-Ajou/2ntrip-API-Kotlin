package com.entrip.service

import com.entrip.domain.dto.PostsComments.PostsCommentsReturnDto
import com.entrip.domain.dto.PostsComments.PostsCommentsSaveRequestDto
import com.entrip.domain.entity.Posts
import com.entrip.domain.entity.PostsComments
import com.entrip.domain.entity.Users
import com.entrip.repository.PostsCommentsRepository
import com.entrip.repository.PostsRepository
import com.entrip.repository.UsersRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class PostsCommentsService(
    val postsCommentsRepository: PostsCommentsRepository,

    val usersRepository: UsersRepository,

    @Autowired
    val postsRepository: PostsRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(PostsCommentsService::class.java)

    private fun findPostsComments(postComment_id: Long): PostsComments {
        val postsComments: PostsComments = postsCommentsRepository.findById(postComment_id).orElseThrow {
            IllegalArgumentException("Error raise at postsCommentsRepository.findById$postComment_id")
        }
        return postsComments
    }

    private fun findUsers(user_id: String?): Users {
        val users: Users = usersRepository.findById(user_id!!).orElseThrow {
            IllegalArgumentException("Error raise at usersRepository.findById$user_id")
        }
        return users
    }

    private fun findPosts(post_id: Long): Posts = postsRepository.findById(post_id).orElseThrow {
        IllegalArgumentException("Error raise at postsRepository.findById $post_id")
    }

    @Transactional
    fun save(requestDto: PostsCommentsSaveRequestDto): Long {
        val postsComments: PostsComments = requestDto.toEntity()
        val users = findUsers(requestDto.author)
        val posts = findPosts(requestDto.post_id)
        postsComments.setAuthorWithJoin(users)
        postsComments.setPostsWithJoin(posts)
        postsCommentsRepository.save(postsComments)
        posts.raiseCommentsNumber()
        return postsComments.postComment_id!!
    }

    fun findById(postComment_id: Long): PostsCommentsReturnDto =
        makePostsCommentsReturnDto(findPostsComments(postComment_id))

    fun getAllCommentsWithPostId(post_id: Long): MutableList<PostsCommentsReturnDto> {
        val posts = findPosts(post_id)
        val postsCommentsSet: MutableSet<PostsComments> = posts.postsCommentsSet!!
        val postsCommentsList: MutableList<PostsCommentsReturnDto> = ArrayList<PostsCommentsReturnDto>()
        val iterator = postsCommentsSet.iterator()
        while (iterator.hasNext()) {
            val postsComments = iterator.next()
            val returnDto = makePostsCommentsReturnDto(postsComments)
            postsCommentsList.add(returnDto)
        }
        postsCommentsList.sort()
        return postsCommentsList
    }

    private fun makePostsCommentsReturnDto(postsComments: PostsComments) : PostsCommentsReturnDto =
        if (checkValidComments(postsComments)) PostsCommentsReturnDto(postsComments)
        else createDummyComments(postsComments)


    private fun checkValidComments(postsComments: PostsComments) : Boolean =
        postsComments.author!=null

    private fun createDummyComments(postsComments: PostsComments) : PostsCommentsReturnDto =
        PostsCommentsReturnDto(
            postComment_id = postsComments.postComment_id!!,
            content = "",
            author = "",
            nickname = ""
        )

    @Transactional
    fun delete(postComment_id: Long): Long {
        val postsComments = findPostsComments(postComment_id)
        val users = postsComments.author
        val posts = postsComments.posts
        return if (postsComments.postsNestedComments!!.isEmpty()) {
            users!!.postsComments.remove(postsComments)
            posts!!.postsCommentsSet!!.remove(postsComments)
            postsCommentsRepository.delete(postsComments)
            posts.decreaseCommentsNumber()
            postComment_id
        } else makeCommentsEmpty(postsComments)
    }

    private fun makeCommentsEmpty(postsComments : PostsComments) : Long {
        postsComments.author = null
        postsComments.content = ""
        postsComments.posts!!.decreaseCommentsNumber()
        return postsComments.postComment_id!!
    }

}