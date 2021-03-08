package com.example.mongodb.messageRepository

import com.example.mongodb.model.Message
import com.example.mongodb.model.TimeTracker
import mu.KLogging
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.Instant

interface MessageRepositoryInterface : MongoRepository<Message, String> {}

@Repository
class MessageRepository(
        private val messageRepositoryInterface: MessageRepositoryInterface,
        private val timeTracker: TimeTracker
) {

    fun save(message: String) {
        val newMessage = Message(key = "1", data = message)
        logger.info { "***Trying to save message: $message" }
        timeTracker.addTiming(Instant.now())
        messageRepositoryInterface.save(newMessage)
    }

    fun getAllMessages(): List<Message> = messageRepositoryInterface.findAll()

    companion object: KLogging()
}
