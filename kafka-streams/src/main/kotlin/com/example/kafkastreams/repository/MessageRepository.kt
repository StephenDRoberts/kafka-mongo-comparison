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

    fun getLocalUsers(): Map<String, String> {
        while (true) {
            try {
                val stateStore = store.getStore()
                val messages = convertKeyValuesToMap(stateStore.all())
                logger.info { messages }
                return messages
            } catch (e: InvalidStateStoreException) {
                // store not yet ready for querying
                logger.info{"retrying..."}
                Thread.sleep(100)
            }
        }
    }

    private fun convertKeyValuesToMap(items: KeyValueIterator<String, String>): Map<String, String> {
        logger.info { "Mapping" }
        logger.info { items }
        val localItemsMap = mutableMapOf<String, String>()

        while (items.hasNext()) {
            val keyValuePair = items.next()
            localItemsMap[keyValuePair.key] = keyValuePair.value
        }
        return localItemsMap
    }

    companion object: KLogging()
}