package com.example.setup.generator

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDate
import javax.annotation.PostConstruct

@Component
class MessageGenerator(
        private val kafkaTemplate: KafkaTemplate<String, Any>
) {

    @PostConstruct
    fun setup() {
        val today = LocalDate.now().toString()
        for(i in 1..5) {
            val message = mapOf(
                    "id" to i.toString(),
                    "sport" to "Football",
                    "competition" to "Premier League",
                    "tags" to listOf("Premier League", "Football"),
                    "event" to mapOf(
                            "homeTeam" to "Team${i}A",
                            "awayTeam" to "Team${i}B",
                            "date" to today
                    )
            )

            kafkaTemplate.send("message-topic", i.toString(), message)
        }
    }
}