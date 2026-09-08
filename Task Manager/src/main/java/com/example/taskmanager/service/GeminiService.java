//package com.example.taskmanager.service;
//
//import com.example.taskmanager.model.Task;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.client.RestClientResponseException;
//
//import java.util.List;
//
//@Service
//public class GeminiService {
//
//    @Value("${gemini.api.key}")
//    private String apiKey;
//
//    private final RestClient restClient = RestClient.create();
//    private final TaskService taskService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public GeminiService(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//    public String askGemini(String userPrompt, String username) {
//
//        // 1. Sirf current logged-in user ke tasks
//        List<Task> tasks = taskService.getTasksByUser(username);
//
//        // 2. Tasks ko AI-readable format mein convert karo
//        StringBuilder taskContext = new StringBuilder();
//
//        if (tasks.isEmpty()) {
//
//            taskContext.append("The user has no tasks.");
//
//        } else {
//
//            for (Task task : tasks) {
//
//                taskContext.append("""
//
//                        Task ID: %s
//                        Title: %s
//                        Description: %s
//                        Date: %s
//                        Status: %s
//
//                        """.formatted(
//                        task.getId(),
//                        task.getTitle(),
//                        task.getDescription(),
//                        task.getDate(),
//                        task.isCompleted() ? "Completed" : "Pending"
//                ));
//            }
//        }
//
//        // 3. Gemini ke liye complete prompt
//        String prompt = """
//                You are a professional AI assistant inside a Task Manager application.
//
//                You are helping the currently logged-in user with their tasks.
//
//                USER'S TASKS:
//                %s
//
//                USER'S QUESTION:
//                %s
//
//                INSTRUCTIONS:
//                - Always respond in clear, professional English, regardless of the language the question is asked in.
//                - Answer based on the user's tasks when the question is task-related.
//                - Do not make up tasks that do not exist.
//                - If the user asks about pending tasks, consider only tasks with status Pending.
//                - If the user asks about completed tasks, consider only tasks with status Completed.
//                - Keep answers concise and well-structured. Prefer short paragraphs or a bullet list over long blocks of text.
//                - Use "- " for bullet points and "**text**" for bold/emphasis when listing tasks or key details.
//                - Do not use emojis, filler phrases, or overly casual language. Keep the tone helpful and professional.
//                - If the user asks a general programming or technical question, answer it normally, following the same tone and formatting rules.
//                """.formatted(taskContext, userPrompt);
//
//        // 4. Gemini API call
//        String url =
//                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent";
//
//        String requestBody = """
//                {
//                  "contents": [
//                    {
//                      "parts": [
//                        {
//                          "text": %s
//                        }
//                      ]
//                    }
//                  ]
//                }
//                """.formatted(toJsonString(prompt));
//
//        try {
//
//            String rawResponse = restClient.post()
//                    .uri(url)
//                    .header("x-goog-api-key", apiKey)
//                    .header("Content-Type", "application/json")
//                    .body(requestBody)
//                    .retrieve()
//                    .body(String.class);
//
//            return extractAnswerText(rawResponse);
//
//        } catch (RestClientResponseException e) {
//
//            System.out.println("Gemini Status: " + e.getStatusCode());
//            System.out.println("Gemini Error: " + e.getResponseBodyAsString());
//
//            throw new RuntimeException(
//                    "Gemini API Error: " + e.getResponseBodyAsString()
//            );
//        }
//    }
//
//    // Gemini response se actual answer nikalta hai
//    private String extractAnswerText(String rawResponse) {
//
//        try {
//
//            JsonNode root = objectMapper.readTree(rawResponse);
//
//            JsonNode textNode = root
//                    .path("candidates")
//                    .path(0)
//                    .path("content")
//                    .path("parts")
//                    .path(0)
//                    .path("text");
//
//            if (textNode.isMissingNode()) {
//                return "Sorry, I couldn't generate a valid response. Please try again.";
//            }
//
//            return textNode.asText();
//
//        } catch (Exception e) {
//
//            System.out.println(
//                    "Failed to parse Gemini response: " + e.getMessage()
//            );
//
//            return "Sorry, something went wrong while processing the response.";
//        }
//    }
//
//    // Prompt ko safely JSON string mein convert karta hai
//    private String toJsonString(String text) {
//
//        return "\"" + text
//                .replace("\\", "\\\\")
//                .replace("\"", "\\\"")
//                .replace("\n", "\\n")
//                .replace("\r", "") + "\"";
//    }
//}

package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestClient restClient = RestClient.create();
    private final TaskService taskService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiService(TaskService taskService) {
        this.taskService = taskService;
    }

    public String askGemini(String userPrompt, String username) {

        // 1. Sirf current logged-in user ke tasks
        List<Task> tasks = taskService.getTasksByUser(username);

        // 2. Tasks ko AI-readable format mein convert karo
        StringBuilder taskContext = new StringBuilder();

        if (tasks.isEmpty()) {

            taskContext.append("The user has no tasks.");

        } else {

            for (Task task : tasks) {

                taskContext.append("""
                        
                        Task ID: %s
                        Title: %s
                        Description: %s
                        Date: %s
                        Status: %s
                        Priority: %s
                        Category: %s
                        
                        """.formatted(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDate(),
                        task.isCompleted() ? "Completed" : "Pending",
                        task.getPriority() != null ? task.getPriority().name() : "None",
                        task.getCategory() != null && !task.getCategory().isBlank() ? task.getCategory() : "Uncategorized"
                ));
            }
        }

        // 3. Gemini ke liye complete prompt
        String prompt = """
                You are a professional AI assistant inside a Task Manager application.

                You are helping the currently logged-in user with their tasks.

                USER'S TASKS:
                %s

                USER'S QUESTION:
                %s

                INSTRUCTIONS:
                - Always respond in clear, professional English, regardless of the language the question is asked in.
                - Answer based on the user's tasks when the question is task-related.
                - Do not make up tasks that do not exist.
                - If the user asks about pending tasks, consider only tasks with status Pending.
                - If the user asks about completed tasks, consider only tasks with status Completed.
                - If the user asks about priority (e.g. "high priority tasks", "what's urgent"), match against each task's Priority field (High, Medium, Low, or None) — not the task title.
                - If the user asks about a category (e.g. "work tasks", "study tasks"), match against each task's Category field — not the task title.
                - Keep answers concise and well-structured. Prefer short paragraphs or a bullet list over long blocks of text.
                - Use "- " for bullet points and "**text**" for bold/emphasis when listing tasks or key details.
                - Do not use emojis, filler phrases, or overly casual language. Keep the tone helpful and professional.
                - If the user asks a general programming or technical question, answer it normally, following the same tone and formatting rules.
                """.formatted(taskContext, userPrompt);

        // 4. Gemini API call
        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent";

        String requestBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": %s
                        }
                      ]
                    }
                  ]
                }
                """.formatted(toJsonString(prompt));

        try {

            String rawResponse = restClient.post()
                    .uri(url)
                    .header("x-goog-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            return extractAnswerText(rawResponse);

        } catch (RestClientResponseException e) {

            System.out.println("Gemini Status: " + e.getStatusCode());
            System.out.println("Gemini Error: " + e.getResponseBodyAsString());

            throw new RuntimeException(
                    "Gemini API Error: " + e.getResponseBodyAsString()
            );
        }
    }

    // Gemini response se actual answer nikalta hai
    private String extractAnswerText(String rawResponse) {

        try {

            JsonNode root = objectMapper.readTree(rawResponse);

            JsonNode textNode = root
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            if (textNode.isMissingNode()) {
                return "Sorry, I couldn't generate a valid response. Please try again.";
            }

            return textNode.asText();

        } catch (Exception e) {

            System.out.println(
                    "Failed to parse Gemini response: " + e.getMessage()
            );

            return "Sorry, something went wrong while processing the response.";
        }
    }

    // Prompt ko safely JSON string mein convert karta hai
    private String toJsonString(String text) {

        return "\"" + text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "") + "\"";
    }
}