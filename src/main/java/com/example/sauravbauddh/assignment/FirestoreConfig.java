package com.example.sauravbauddh.assignment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirestoreConfig {

    private String serviceAccountKey; // Variable to store the service account key JSON content

    @PostConstruct
    public void init() {
        // Read the service account key JSON content from the environment variable
        serviceAccountKey = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY");
    }

    @Bean
    public Firestore firestore() throws IOException {
        // Initialize Firebase Admin SDK with the service account key JSON content
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(serviceAccountKey.getBytes())))
                .build();
        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }
}
