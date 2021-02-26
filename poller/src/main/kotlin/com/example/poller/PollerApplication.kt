package com.example.poller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PollerApplication

fun main(args: Array<String>) {
	runApplication<PollerApplication>(*args)
}
