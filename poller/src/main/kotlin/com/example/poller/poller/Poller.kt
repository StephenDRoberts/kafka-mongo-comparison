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
        appendToCSV(file, 1, readDuration)
        println("done")
    }

    fun createRequest(port: String, urlPath: String): Request {
        return Request.Builder()
                .url("http://localhost:${port}${urlPath}")
                .build()
    }

    fun getReadAllDuration(port :String): Long {
        val startTime = Instant.now()
        val response = okHttpClient.newCall(createRequest(port, "/messages")).execute()
        val endTime = Instant.now()
        return endTime.toEpochMilli() - startTime.toEpochMilli()
    }

    fun getAvgWriteDuration(port: String) {
        val response = okHttpClient.newCall(createRequest(port, "/messages/timings/summary")).execute()
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

