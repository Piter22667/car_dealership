package org.example.car_dealership.service;

import org.example.car_dealership.dto.PromptDTO;

public interface ChatGPTService {
    String getChatResponse(PromptDTO userInput);

}
