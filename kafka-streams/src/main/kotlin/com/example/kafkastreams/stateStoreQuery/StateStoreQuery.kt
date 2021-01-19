package com.example.kafkastreams.stateStoreQuery

import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Component

@Component
class StateStoreQuery(
        private val streamsBuilderFactoryBean: StreamsBuilderFactoryBean
) {
    fun getStore(): ReadOnlyKeyValueStore<String, String> {
        return streamsBuilderFactoryBean
                .kafkaStreams
                .store(StoreQueryParameters.fromNameAndType("message-store", QueryableStoreTypes.keyValueStore<String, String>()))
    }

}