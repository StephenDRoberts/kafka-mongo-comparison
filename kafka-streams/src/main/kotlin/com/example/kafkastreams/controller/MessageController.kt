package com.example.kafkastreams.controller

import com.example.kafkastreams.model.TimeTracker
import com.example.kafkastreams.model.TimingsSummary
import com.example.kafkastreams.repository.MessageRepository
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/messages")
class MessageController(
        private val messageRepository: MessageRepository,
        private val timeTracker: TimeTracker
) {

    // Retrieve all messages - used by Poller for read performance
    @GetMapping
    fun getMessages() : Map<String, String> {
        logger.info { "Retrieving messages" }
        return messageRepository.getLocalMessages()
    }

    // Retrieve a list of timings
    @GetMapping("/timings")
    fun getTimings(): MutableList<Instant> {
        return timeTracker.getAllTimings()
    }

    // Used by the Poller application to retrieve a summary of write statistics
    @GetMapping("/timings/summary")
    fun getTimingsSummary(): TimingsSummary {
        val allTimings = timeTracker.getAllTimings()
        val min = allTimings.minOrNull()
        val max = allTimings.maxOrNull()
        val count = allTimings.size -1
        val totalTime = min?.toEpochMilli()?.let { max?.toEpochMilli()?.minus(it) }
        val avgTime = totalTime?.toFloat()?.div(count.toFloat())
        return TimingsSummary(min, max, count, totalTime, avgTime)
    }

    companion object: KLogging()
}
