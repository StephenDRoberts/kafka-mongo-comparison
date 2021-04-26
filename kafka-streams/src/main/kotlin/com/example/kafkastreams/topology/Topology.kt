package com.example.kafkastreams.topology

import com.example.kafkastreams.model.TimeTracker
import mu.KLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.state.Stores
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.kafka.core.CleanupConfig
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class Topology (
    private val builder: StreamsBuilder,
    private val timeTracker: TimeTracker
    ) {
    init {
        val stringSerde = Serdes.String()

        // Consume from topic
        builder.stream("message-topic", Consumed.with(stringSerde, stringSerde))
                .map { key, value ->
                    KeyValue(key, value)
                }
                // Start timing transaction
                .peek { _, value ->
                    logger.info { "Placing message to State store: $value" }
                    timeTracker.addTiming(Instant.now())
                }
                // Write to persistent state-store
                .toTable(
                        Materialized.`as`<String, String>(Stores.persistentKeyValueStore("message-store"))
                                .withKeySerde(stringSerde)
                                .withValueSerde(stringSerde)
                )
    }
    companion object : KLogging()
}
