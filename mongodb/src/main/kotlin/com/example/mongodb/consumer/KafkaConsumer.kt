package com.example.mongodb.consumer

import com.example.mongodb.messageRepository.MessageRepository
import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(private val messageRepository: MessageRepository) {

    @KafkaListener(id = "mongo-consumer", topics = ["message-topic"])
    fun readMessages(message: String) {
        logger.info { "Receiving message: $message"}
        messageRepository.placeMessageToDB(message)

    }

    companion object : KLogging()
}