package com.example.mongodb.controller

import com.example.mongodb.messageRepository.MessageRepository
import com.example.mongodb.model.Message
import com.example.mongodb.model.TimeTracker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant

internal class MessageControllerTest {
    private val messageRepository = mockk<MessageRepository>()
    private val timeTracker = mockk<TimeTracker>()
    private val underTest = MessageController(messageRepository, timeTracker)

    private val messageString = mapOf(
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
    inner class `Get all messages` {
        @Test
        fun `should delegate request to messageRepository to getAllMessages`() {
            every { messageRepository.getAllMessages() } returns listOf(message)

            val response = underTest.getAllMessages()

            assertThat(response).isEqualTo(listOf(message))
        }
    }

    @Nested
    inner class `Get timings summary` {
        private val jan1st = Instant.parse("2021-01-01T00:00:00.00Z")
        private val oneMin = 60L * 1000L
        private val jan1stPlusOneMin = jan1st.plusMillis(oneMin)

        @Test
        fun `should delegate request to timeTracker for timings summary`() {
            every { timeTracker.getAllTimings() } returns mutableListOf(jan1st, jan1stPlusOneMin)

            underTest.getTimingsSummary()

            verify { timeTracker.getAllTimings() }
        }

        @Test
        fun `should format timings into summary`() {
            every { timeTracker.getAllTimings() } returns mutableListOf(jan1st, jan1stPlusOneMin)

            val response = underTest.getTimingsSummary()

            assertThat(response.min).isEqualTo(jan1st)
            assertThat(response.max).isEqualTo(jan1stPlusOneMin)
            assertThat(response.count).isEqualTo(1)
            assertThat(response.totalTime).isEqualTo(oneMin)
            assertThat(response.avgTime).isEqualTo(oneMin.toFloat())
        }
    }
}