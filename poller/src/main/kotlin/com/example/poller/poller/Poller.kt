package com.example.poller.poller

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.time.Instant
import javax.annotation.PostConstruct

@Component
class Poller() {

    private val okHttpClient = OkHttpClient()

    fun inputPrompt() {
        println("Choose which application you are running (1 or 2):")
        println("1. Kafka Streams")
        println("2. MongoDB")
    }

    fun inputValidationCheck(input: String?): Boolean = input == "1" || input =="2"

    @PostConstruct
    fun poller() {
        var passInputValidation = false

        while(!passInputValidation) {
            inputPrompt()
            val input = readLine()
            passInputValidation = inputValidationCheck(input)
        }


            val newFileCreated = File("poller/src/main/resources/results/kafkaStreams.csv").createNewFile()
            val file = File("poller/src/main/resources/results/kafkaStreams.csv")

            if (newFileCreated) {
                createHeaders(file)
            }
            val avgWriteDuration = getAvgWriteDuration()
            val readDuration = getReadAllDuration()
            appendToCSV(file, 1, readDuration)
            println("done")
        }

    fun createRequest(urlPath: String): Request {
        return Request.Builder()
                .url("http://localhost:8087$urlPath")
                .build()
    }

    fun getReadAllDuration(): Long {
        val startTime = Instant.now()
        val response = okHttpClient.newCall(createRequest("/messages")).execute()
        val endTime = Instant.now()
        return endTime.toEpochMilli() - startTime.toEpochMilli()
    }

    fun getAvgWriteDuration() {
        val response = okHttpClient.newCall(createRequest("/messages/timings/summary")).execute()
//        val responseBody = response.body?.string()

        println("AvgDuration")
        println(response.body?.string())
    }


    fun appendToCSV(file: File, avgWriteDuration: Long,  readDuration: Long) {
//        val resultsFile = Paths.get("poller/src/main/resources/results/$filename").toFile()
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

