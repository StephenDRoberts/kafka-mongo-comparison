package com.example.mongodb.messageRepository

import com.example.mongodb.model.Message
import com.example.mongodb.model.TimeTracker
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

internal class MessageRepositoryTest {
    private val messageRepositoryInterface = mockk<MessageRepositoryInterface>()
    private val timeTracker = mockk<TimeTracker>()
    private val underTest = MessageRepository(messageRepositoryInterface, timeTracker)

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

    val message = Message("1", messageString)

    @Nested
    inner class `Get All Messages` {
        @Test
        fun `should return a list of messages`() {
            every { messageRepositoryInterface.findAll() } returns listOf(message)

            val response = underTest.getAllMessages()

            assertThat(response[0]).isEqualTo(message)
        }
    }

    @Nested
    inner class `Save Messages` {
        @Test
        fun `should call the DB to save a message`() {
            every { timeTracker.addTiming(any()) } just runs
            every { messageRepositoryInterface.save(any()) } returnsArgument 0

            underTest.save(messageString)

            verify { messageRepositoryInterface.save(any()) }


        }

        @Test
        fun `should call timeTracker to add a timing`() {
            every { timeTracker.addTiming(any()) } just runs
            every { messageRepositoryInterface.save(any()) } returnsArgument 0

            underTest.save(messageString)

            verify { timeTracker.addTiming(any()) }
        }
    }

}