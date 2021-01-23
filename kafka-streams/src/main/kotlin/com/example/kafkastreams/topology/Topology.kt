package com.example.kafkastreams.topology

import mu.KLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.state.Stores
import org.springframework.stereotype.Component

@Component
class Topology (
    private val builder: StreamsBuilder
    ) {

    init {
        val stringSerde = Serdes.String()

        builder.stream("message-topic", Consumed.with(stringSerde, stringSerde))
                .map { key, value ->
                    KeyValue(key, value)
                }
                .peek { _, value -> logger.info { "Placing message to State store: $value" } }
                .toTable(
                        Materialized.`as`<String, String>(Stores.persistentKeyValueStore("message-store"))
                                .withKeySerde(stringSerde)
                                .withValueSerde(stringSerde)
                )
    }
    companion object : KLogging()
}
