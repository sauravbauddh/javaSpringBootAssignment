package com.example.sauravbauddh.assignment;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@EnableScheduling
@SpringBootApplication
public class AssignmentApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(AssignmentApplication.class, args);
    }
}
