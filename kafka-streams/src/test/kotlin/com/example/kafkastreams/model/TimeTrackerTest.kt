package com.example.kafkastreams.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

internal class TimeTrackerTest {
    private val underTest = TimeTracker()
    private val today = Instant.now()

    @Test
    fun `should add timings to list`() {
        underTest.addTiming(today)
        underTest.addTiming(today)
        underTest.addTiming(today)

        assertThat(underTest.timings.size).isEqualTo(3)
    }

    @Test
    fun `should return a list of timings`() {
        underTest.addTiming(today)
        underTest.addTiming(today)
        underTest.addTiming(today)

        assertThat(underTest.getAllTimings()).isEqualTo(mutableListOf(today, today, today))
    }
}