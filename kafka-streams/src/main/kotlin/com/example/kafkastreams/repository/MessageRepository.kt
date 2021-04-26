package com.example.kafkastreams.repository

import com.example.kafkastreams.stateStoreQuery.StateStoreQuery
import mu.KLogging
import org.apache.kafka.streams.errors.InvalidStateStoreException
import org.apache.kafka.streams.state.KeyValueIterator
import org.springframework.stereotype.Component

@Component
class MessageRepository(
        private val store: StateStoreQuery,
) {

    fun getLocalMessages(): Map<String, String> {
        while (true) {
            try {
                // Get the state store
                val stateStore = store.getStore()

                // Gets all values from state store and converts from a
                // KeyValueIterator (Kafka specific) to a Map
                return convertKeyValuesToMap(stateStore.all())
            } catch (e: InvalidStateStoreException) {
                // store not yet ready for querying
                logger.info{"retrying..."}
                Thread.sleep(100)
            }
        }
    }

    private fun convertKeyValuesToMap(items: KeyValueIterator<String, String>): Map<String, String> {
        val localItemsMap = mutableMapOf<String, String>()

        while (items.hasNext()) {
            val keyValuePair = items.next()
            localItemsMap[keyValuePair.key] = keyValuePair.value
        }
        return localItemsMap
    }

    companion object: KLogging()
}