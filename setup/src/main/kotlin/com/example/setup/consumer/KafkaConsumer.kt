package com.example.setup.consumer

import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaConsumer {

    @KafkaListener(id = "kafka-consumer", topics = ["message-topic"])
    fun readMessages(message: String) {
        logger.info { "Receiving message: $message"}
    }

    companion object : KLogging()
}