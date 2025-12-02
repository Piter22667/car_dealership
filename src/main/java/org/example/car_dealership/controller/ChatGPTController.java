package org.example.car_dealership.controller;

import org.example.car_dealership.dto.PromptDTO;
import org.example.car_dealership.service.impl.ChatGPTServiceImpl;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatGPTController {
    private final ChatGPTServiceImpl chatGPTServiceImpl;

    public ChatGPTController(ChatGPTServiceImpl chatGPTServiceImpl) {
        this.chatGPTServiceImpl = chatGPTServiceImpl;
    }

    @PostMapping
    public String chat(@RequestBody PromptDTO userInput) {
        return chatGPTServiceImpl.getChatResponse(userInput);
    }
}