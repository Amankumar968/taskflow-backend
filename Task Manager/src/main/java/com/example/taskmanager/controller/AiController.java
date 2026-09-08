package com.example.taskmanager.controller;

import com.example.taskmanager.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AiController {

    private final GeminiService geminiService;

    public AiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(
            @RequestBody Map<String, String> request,
            Authentication authentication
    ) {

        String prompt = request.get("prompt");

        if (prompt == null || prompt.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Prompt cannot be empty"));
        }

        try {

            // Logged-in user ka username JWT/Spring Security se
            String username = authentication.getName();

            // Prompt + username GeminiService ko bhejo
            String response = geminiService.askGemini(prompt, username);

            return ResponseEntity.ok(
                    Map.of("response", response)
            );

        } catch (Exception e) {

            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error", "AI request failed",
                            "message", e.getMessage()
                    ));
        }
    }
}