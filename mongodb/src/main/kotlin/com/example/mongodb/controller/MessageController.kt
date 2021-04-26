package com.example.mongodb.controller

import com.example.mongodb.messageRepository.MessageRepository
import com.example.mongodb.model.Message
import com.example.mongodb.model.TimeTracker
import com.example.mongodb.model.TimingsSummary
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

    // Used by the Poller for read performance
    @GetMapping
    fun getAllMessages(): List<Message> = messageRepository.getAllMessages()

    // Retrieve a list of timings
    @GetMapping("/timings")
    fun getTimings(): List<Instant> = timeTracker.getAllTimings()

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
}