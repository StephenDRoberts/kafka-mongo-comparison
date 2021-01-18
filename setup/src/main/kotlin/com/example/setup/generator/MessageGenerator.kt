package com.example.setup.generator

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class MessageGenerator(
        private val kafkaTemplate: KafkaTemplate<String, String>
) {

    @PostConstruct
    fun setup() {
        kafkaTemplate.send("message-topic", "1", "Steve")
        kafkaTemplate.send("message-topic", "2", "Another Steve")
        kafkaTemplate.send("message-topic", "3", "Not Steve")
    }
}