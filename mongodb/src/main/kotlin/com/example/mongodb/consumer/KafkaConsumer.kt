package com.example.mongodb.consumer

import com.example.mongodb.messageRepository.MessageRepository
import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer(private val messageRepository: MessageRepository) {

    @KafkaListener(id = "mongo-consumer", topics = ["message-topic"])
    fun readMessagesFromKafka(message: String) {
        messageRepository.save(message)
    }

    companion object : KLogging()
}