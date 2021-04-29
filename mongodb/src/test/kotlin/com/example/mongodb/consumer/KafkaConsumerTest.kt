package com.example.mongodb.consumer

import com.example.mongodb.messageRepository.MessageRepository
import com.example.mongodb.model.Message
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class KafkaConsumerTest {
    private val messageRepository = mockk<MessageRepository>()
    private val underTest = KafkaConsumer(messageRepository)

    val messageString = mapOf(
            "id" to 1,
            "sport" to "Football",
            "competition" to "Premier League",
            "tags" to listOf("Premier League", "Football"),
            "event" to mapOf(
                    "homeTeam" to "Team1A",
                    "awayTeam" to "Team1B",
                    "date" to "2021-03-08T14:38:14.078632"
            )
    ).toString()

    @Test
    fun `should delegate request to messageRepository`() {
        every { messageRepository.save(any()) } returnsArgument 0

        underTest.readMessagesFromKafka(messageString)

        verify { messageRepository.save(any()) }
    }

}