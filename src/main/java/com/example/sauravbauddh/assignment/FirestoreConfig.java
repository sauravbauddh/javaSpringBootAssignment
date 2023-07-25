package com.example.sauravbauddh.assignment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore() throws IOException {
        // Path to your service account key JSON file
        String serviceAccountPath = "C:/Users/golub/serviceAccountKey.json";

        // Initialize Firebase Admin SDK
        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }
}
