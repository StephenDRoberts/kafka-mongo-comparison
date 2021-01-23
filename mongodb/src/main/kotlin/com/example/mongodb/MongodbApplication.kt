package com.example.mongodb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class MongodbApplication

fun main(args: Array<String>) {
	runApplication<MongodbApplication>(*args)
}
