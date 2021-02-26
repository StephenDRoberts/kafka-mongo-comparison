package com.example.mongodb.model

import java.time.Instant

data class TimingsSummary (
    val min: Instant?,
    val max: Instant?,
    val count: Int,
    val totalTime: Long?,
    val avgTime: Float?
)