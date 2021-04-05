package com.example.poller.poller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.Instant
import javax.annotation.PostConstruct

@Component
class Poller() {

    private val okHttpClient = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    private val applicationOptions = mapOf(
        "1" to mapOf(
                "name" to "Kafka Streams",
                "id" to "kafkaStreams",
                "port" to 8087
        ),
        "2" to mapOf(
                "name" to "MongoDB",
                "id" to "mongoDb",
                "port" to 8088
        )
    )

    fun inputPrompt() {
        println("Choose which application you are running (1 or 2):")
        for (option in applicationOptions){
            println("${option.key}. ${option.value["name"]}")
        }
    }

    fun inputValidationCheck(input: String?): Boolean = input == "1" || input =="2"

    @PostConstruct
    fun poller() {
        var passInputValidation = false
        var input = ""
        while(!passInputValidation) {
            inputPrompt()
            input = readLine().toString()
            passInputValidation = inputValidationCheck(input)
        }

        val id = applicationOptions[input]?.get("id")
        val port = applicationOptions[input]?.get("port").toString()

        val newFileCreated = File("poller/src/main/resources/results/${id}.csv").createNewFile()
        val file = File("poller/src/main/resources/results/${id}.csv")

        if (newFileCreated) {
            createHeaders(file)
        }
        val avgWriteDuration = getAvgWriteDuration(port)
        val readDuration = getReadAllDuration(port)
        appendToCSV(file = file, avgWriteDuration = avgWriteDuration , readDuration = readDuration)
        println("done")
    }

    fun createRequest(port: String, urlPath: String): Request {
        return Request.Builder()
                .url("http://localhost:${port}${urlPath}")
                .build()
    }

    fun getReadAllDuration(port :String): String {
        val startTime = Instant.now()
        val response = okHttpClient.newCall(createRequest(port, "/messages")).execute()
        val endTime = Instant.now()
        return (endTime.toEpochMilli() - startTime.toEpochMilli()).toString()
    }

    fun getAvgWriteDuration(port: String): String {
        val response = okHttpClient.newCall(createRequest(port, "/messages/timings/summary")).execute()
        val parsedResponse = parseResponse(response)
        val avgWriteDuration = parsedResponse["avgTime"]

        return avgWriteDuration.toString()
    }

    fun parseResponse(response: Response): JsonNode {
        val body = response.body?.string() ?: ""
        val jsonBody = objectMapper.readValue<JsonNode>(body)
        return jsonBody
    }

    fun appendToCSV(file: File, avgWriteDuration: String,  readDuration: String) {
        val fileWriter = FileWriter(file, true)

        try {
            fileWriter.append("\n")
            fileWriter.append("${avgWriteDuration},${readDuration}")
            println("Wrote to CSV successfully!")
        } catch (e: Exception) {
            println("Writing CSV error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }
    }

    fun createHeaders(file: File) {
        val fileWriter = FileWriter(file, true)

        try {
            fileWriter.append("Kafka Stream Results")
            fileWriter.append("\n")
            fileWriter.append("Avg Write Duration,Read Duration")
            println("Created headers!")
        } catch (e: Exception) {
            println("Writing CSV headers error!")
            e.printStackTrace()
        } finally {
            try {
                fileWriter.flush()
                fileWriter.close()
            } catch (e: IOException) {
                println("Flushing/closing error!")
                e.printStackTrace()
            }
        }
    }





//    fun parseResponse(response: Response): DTO {
//        val body = response.body?.string() ?: ""
//        val jsonBody = objectMapper.readValue<DTO>(body)
//        return jsonBody
//    }
}

