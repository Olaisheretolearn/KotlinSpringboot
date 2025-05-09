package com.summerlockin.GenNoteApp.database.repository

import com.summerlockin.GenNoteApp.database.model.Note
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository  : MongoRepository<Note, ObjectId> {
    fun findByOwnerId (ownerId:ObjectId) : List<Note>


}

