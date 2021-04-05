package com.example.mongodb.integrationTests

import com.example.mongodb.MongodbApplication
import com.example.mongodb.messageRepository.MessageRepository
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = ["message-topic"], bootstrapServersProperty = "spring.kafka.bootstrap-servers", brokerProperties = ["listeners=PLAINTEXT://localhost:9093", "port=9093"])
@AutoConfigureDataMongo
class IntegrationTests {

    val message = mapOf(
            "id" to 1,
            "sport" to "Football",
            "competition" to "Premier League",
            "tags" to listOf("Premier League", "Football"),
            "event" to mapOf(
                    "homeTeam" to "Team1A",
                    "awayTeam" to "Team1B",
                    "date" to "2021-03-08T14:38:14.078632"
            )
    ).toString()

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

//    @Autowired
//    private lateinit var mongoTemplate: MongoTemplate

//    private lateinit var consumer: Consumer<String, String>
    private lateinit var producer: Producer<String, String>

    @Autowired
    private lateinit var repository: MessageRepository



    @BeforeEach
    fun setup() {
        val consumerProps = KafkaTestUtils.consumerProps("kafka-mongo-comparison", "true", embeddedKafkaBroker)
//        val consumerFactory: ConsumerFactory<String, String> = DefaultKafkaConsumerFactory<String, String>(consumerProps, StringDeserializer(), StringDeserializer())
        val producerFactory: ProducerFactory<String, String> = DefaultKafkaProducerFactory<String, String>(consumerProps, StringSerializer(), StringSerializer())
//        consumer = consumerFactory.createConsumer()
        producer = producerFactory.createProducer()
//        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "user-topic")
    }

    @Test
    fun `should put a message to the user-topic`() {
        producer.send(ProducerRecord("message-topic", "1", message))

//        consumer.readMessagesFromKafka()
//        val replies = KafkaTestUtils.getRecords<String, User>(consumer, 100, 2)
//        assertThat(replies.count()).isGreaterThanOrEqualTo(2)

//        val records = mongoTemplate.findAll(DBObject.class,"collection")
        val records = repository.getAllMessages()
        assertThat(records.size).isEqualTo(1)
    }
}