package com.example.taskmanager.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FirebaseConfig {

    // Local dev: defaults to the JSON file sitting in src/main/resources
    // (never committed to git — it's in .gitignore).
    // Production (Render): set FIREBASE_CREDENTIALS_PATH to
    // "file:/etc/secrets/firebase-service-account.json" — the path where
    // Render mounts a Secret File — so the real key never has to live in
    // the repo or a plain environment variable.
    @Value("${firebase.credentials.path:classpath:firebase-service-account.json}")
    private String credentialsPath;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @PostConstruct
    public void initialize() {
        try {
            Resource resource = resourceLoader.getResource(credentialsPath);

            try (InputStream serviceAccount = resource.getInputStream()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase: " + e.getMessage());
        }
    }
}