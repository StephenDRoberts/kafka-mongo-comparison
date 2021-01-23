package com.example.mongodb.messageRepository

import com.example.mongodb.model.Message
import mu.KLogging
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

interface MessageRepositoryInterface : MongoRepository<Message, String> {}

@Repository
class MessageRepository(
        private val messageRepositoryInterface: MessageRepositoryInterface
) {

    fun placeMessageToDB(message: String){
        logger.info { "***Trying to save message: $message" }
        val newMessage = Message(key = "1", data = message)
        messageRepositoryInterface.save(newMessage)
    }
    companion object: KLogging()
}
