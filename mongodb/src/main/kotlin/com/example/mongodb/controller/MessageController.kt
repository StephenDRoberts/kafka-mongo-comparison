package com.example.mongodb.controller

import com.example.mongodb.messageRepository.MessageRepository
import com.example.mongodb.model.Message
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/messages")
class MessageController(private val messgaeRepository: MessageRepository) {

    @GetMapping
    fun getAllMessages(): List<Message> = messgaeRepository.getAllMessages()
}