package com.example.taskmanager.config;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FirebaseConfig {

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @PostConstruct
    public void initialize() {
        try {
            // Directly check Render environment variable first, fallback to local dev path
            String path = System.getenv("FIREBASE_CREDENTIALS_PATH");
            if (path == null || path.isEmpty()) {
                path = "classpath:firebase-service-account.json";
            }

            Resource resource = resourceLoader.getResource(path);

            try (InputStream serviceAccount = resource.getInputStream()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        // Explicitly force the JDK-based NetHttpTransport instead of
                        // letting the SDK auto-detect a transport from the classpath.
                        // Auto-detection can still pick ApacheHttpTransport (via
                        // google-http-client-apache-v2 pulled in transitively by
                        // google-cloud-storage / google-cloud-firestore, which ship
                        // inside firebase-admin), which double-decompresses gzip
                        // responses when fetching Google's public key certificates,
                        // causing: "Error while fetching public key certificates:
                        // Not in GZIP format".
                        .setHttpTransport(new NetHttpTransport())
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