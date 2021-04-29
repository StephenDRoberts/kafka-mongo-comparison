package com.example.kafkastreams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.EnableKafkaStreams

@SpringBootApplication
@EnableKafka
@EnableKafkaStreams
class KafkaStreamsApplication

fun main(args: Array<String>) {
	runApplication<KafkaStreamsApplication>(*args)
}
