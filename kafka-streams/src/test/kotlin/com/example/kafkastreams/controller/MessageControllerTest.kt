package com.example.kafkastreams.controller

import com.example.kafkastreams.model.TimeTracker
import com.example.kafkastreams.repository.MessageRepository
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

    val message = mapOf(
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


    @Nested
    inner class `Get all messages` {
        @Test
        fun `should delegate request to messageRepository to getLocalMessages`(){
            every { messageRepository.getLocalMessages() } returns mapOf("1" to message)

            val response = underTest.getMessages()

            assertThat(response).isEqualTo(mapOf("1" to message))
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