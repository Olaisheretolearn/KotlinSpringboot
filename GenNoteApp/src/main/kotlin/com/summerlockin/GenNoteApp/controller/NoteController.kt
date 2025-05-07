package com.summerlockin.GenNoteApp.controller

import com.summerlockin.GenNoteApp.database.model.Note
import com.summerlockin.GenNoteApp.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/notes")
class NoteController(
    private  val repository: NoteRepository
) {

    //what needs to be in your request
    data class NoteRequest(
        val id : String?,
        val title : String,
        val content: String,
        val color:Long,
        val ownerId:String
    )

    data class NoteResponse(
        val id : String,
        val title:String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )

    @PostMapping
     fun save(body :NoteRequest): NoteResponse {
       var note =  repository.save(
            Note(
                id = body.id?.let{  ObjectId(it) }?: ObjectId.get() ,
              title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId(body.ownerId)
            )
        )

       return note.toResponse()
     }

    @GetMapping
    fun findByOwnerId(
        @RequestParam(required = true) ownerId: String
    ): List<NoteResponse> {
        return repository.findByOwnerId(ObjectId(ownerId)).map {
            it.toResponse()
        }
    }

    fun Note.toResponse(): NoteController.NoteResponse {
        return NoteResponse(
            id  = id.toHexString(),
            title = title,
            content = content,
            color = color,
            createdAt = createdAt,

            )
    }
}