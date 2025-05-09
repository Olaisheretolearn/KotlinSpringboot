package com.summerlockin.GenNoteApp.controller

import com.summerlockin.GenNoteApp.database.model.Note
import com.summerlockin.GenNoteApp.database.repository.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.*
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

    )
    //delete DELETE http://localhost:8080/note/id

    data class NoteResponse(
        val id : String,
        val title:String,
        val content: String,
        val color: Long,
        val createdAt: Instant
    )

    @PostMapping
     fun save(
        @RequestBody body :NoteRequest): NoteResponse {
       var note =  repository.save(
            Note(
                id = body.id?.let{  ObjectId(it) }?: ObjectId.get() ,
              title = body.title,
                content = body.content,
                color = body.color,
                createdAt = Instant.now(),
                ownerId = ObjectId()
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

    @DeleteMapping(path = ["/{id}"])

    fun deleteById (
        @PathVariable  id: String) {
        repository.deleteById(ObjectId(id))
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