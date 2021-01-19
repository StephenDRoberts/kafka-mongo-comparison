package com.example.kafkastreams.controller

import com.example.kafkastreams.repository.MessageRepository
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
class MessageController(
        private val messageRepository: MessageRepository
) {

    @GetMapping
    fun getMessages() : Map<String, String>{
        logger.info { "Retrieving messages" }
        return messageRepository.getLocalUsers()
    }
    companion object: KLogging()
}



