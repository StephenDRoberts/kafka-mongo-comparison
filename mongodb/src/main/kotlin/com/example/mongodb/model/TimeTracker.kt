package com.example.mongodb.model

import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TimeTracker(){
    val timings = mutableListOf<Instant>()

    fun addTiming(instant: Instant) {
        timings.add(instant)
    }
    fun getAllTimings(): MutableList<Instant> {
        return timings
    }
}

