package com.example.kafkastreams.topology

import com.example.kafkastreams.model.TimeTracker
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TopologyTestDriver
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TopologyTest() {
    private lateinit var testDriver: TopologyTestDriver
    private lateinit var messageTopic: TestInputTopic<String, String>
    private lateinit var messageStore: ReadOnlyKeyValueStore<String, String>

    private val timeTracker = mockk<TimeTracker>()

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

    @BeforeEach
    fun setup(){
        val config = mapOf(
                StreamsConfig.APPLICATION_ID_CONFIG to "message-service",
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG to "mock:1234",
                StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG to Serdes.String().javaClass.name,
                StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG to Serdes.String().javaClass.name,
        ).toProperties()

        val builder = StreamsBuilder()
        val messageTopology = Topology(builder, timeTracker)

        testDriver = TopologyTestDriver(builder.build(), config)
        val stringSerde = Serdes.String()
        messageTopic = testDriver.createInputTopic("message-topic", stringSerde.serializer(), stringSerde.serializer())
        messageStore = testDriver.getKeyValueStore("message-store")
    }

    @AfterEach
    fun cleanup() {
        testDriver.close()
    }

    @Test
    fun `should put a message to the state store`() {
        every { timeTracker.addTiming(any()) } just runs
        messageTopic.pipeInput("1", message)

        assertThat(messageStore.approximateNumEntries()).isEqualTo(1)
    }

}