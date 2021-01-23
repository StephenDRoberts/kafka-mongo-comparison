package com.example.mongodb.config

import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Configuration
class SpringMongoConfig (
    @Value("\${spring.data.mongo.database}") val databaseName: String,
    @Value("\${spring.data.mongo.host}") val mongoHost: String,
    @Value("\${spring.data.mongo.port}") val mongoPort: Int,
    @Value("\${spring.data.mongo.user}") val mongoUser: String,
    @Value("\${spring.data.mongo.password}") val mongoPassword: String
    ) {

        @Bean
        fun mongoDbFactory(): MongoDatabaseFactory {
            val mongoClient = MongoClients.create("mongodb://${mongoUser}:${mongoPassword}@${mongoHost}:${mongoPort}")
            return SimpleMongoClientDatabaseFactory(mongoClient, databaseName)
        }

        @Bean
        fun mongoTemplate(): MongoTemplate = MongoTemplate(mongoDbFactory())


    }


