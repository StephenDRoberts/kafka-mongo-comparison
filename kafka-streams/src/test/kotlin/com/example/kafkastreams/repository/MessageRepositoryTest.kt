package com.example.kafkastreams.repository

import com.example.kafkastreams.stateStoreQuery.StateStoreQuery
import io.mockk.every
import io.mockk.mockk
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.state.KeyValueIterator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class MessageRepositoryTest {
    private val store = mockk<StateStoreQuery>()
    private val keyValueIterator = mockk<KeyValueIterator<String, String>>()

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

    private val underTest = MessageRepository(store)


    @Nested
    inner class `Get Local Messages` {
        @Test
        fun `should return a map of messages`() {
            every { store.getStore().all() } returns keyValueIterator
            every { keyValueIterator.hasNext() } returns true andThen false
            every { keyValueIterator.next() } returns KeyValue("1", message)

            val response = underTest.getLocalMessages()

            assertThat(response["1"]).isEqualTo(message)
        }
    }
}