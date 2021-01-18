package com.example.setup

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class SetupApplication

fun main(args: Array<String>) {
	runApplication<SetupApplication>(*args)
}
