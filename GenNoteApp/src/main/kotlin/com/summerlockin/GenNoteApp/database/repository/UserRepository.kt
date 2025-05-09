package com.summerlockin.GenNoteApp.database.repository

import com.summerlockin.GenNoteApp.database.model.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, ObjectId>{
    fun findByEmail(email: String) :User?
}