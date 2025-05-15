package com.martin.bookstore.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.bookstore.enums.PromptTemplate;
import com.openai.client.OpenAIClient;
import com.openai.errors.RateLimitException;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenaiService {

    private static final Logger log = LoggerFactory.getLogger(OpenaiService.class);
    private final OpenAIClient openAIClient;

    public List<String> getSuggestedTitles(String userInput) {
        String prompt = PromptTemplate.DEFAULT_PROMPT.getPrompt() + userInput;

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_4O)
                .temperature(0.8)
                .build();

        ChatCompletion chatCompletion = openAIClient
                .chat()
                .completions()
                .create(params);

        String jsonContent = chatCompletion.choices().stream()
                .findFirst()
                .flatMap(choice -> choice.message().content())
                .orElse("");

        System.out.println("OPENAI RESPONSE: " + jsonContent);

        return parseTitlesFromJson(jsonContent);
    }

    private List<String> parseTitlesFromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            JsonNode booksNode = root.get("books");

            if (booksNode != null && booksNode.isArray()) {
                List<String> result = new ArrayList<>();
                for (JsonNode node : booksNode) {
                    result.add(node.asText());
                }
                return result;
            }
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
        }
        return new ArrayList<>();
    }


    public String getTestCompletion(String prompt) {
        try {
            ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                    .addUserMessage(prompt)
                    .model(ChatModel.GPT_3_5_TURBO)
                    .build();

            ChatCompletion chat = openAIClient
                    .chat()
                    .completions()
                    .create(params);

            return chat.choices().stream()
                    .findFirst()
                    .flatMap(c -> c.message().content())
                    .orElse("");
        } catch (RateLimitException ex) {
            log.warn("OpenAI rate limit exceeded", ex);
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "AI service is temporarily unavailable (quota exceeded). Please try again later."
            );
        } catch (Exception ex) {
            log.error("Unexpected error calling OpenAI", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to get AI response"
            );
        }
    }
}