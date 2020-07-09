package com.accommodation.system.config;

import com.accommodation.system.define.Constant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;

//@Configuration
public class FirebaseConfig {
    @Bean
    public void initializeFirebaseApp () {
        try {
//            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//
//            InputStream serviceAccount = classLoader.getResourceAsStream(Constant.FirebaseProject.SERVICE_ACCOUNT_FILE_NAME);
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//            FirebaseApp.initializeApp(options);
            FileInputStream serviceAccount =
                    new FileInputStream(Constant.FirebaseProject.SERVICE_ACCOUNT_FILE_NAME);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://doantotnghiep-2a3f4.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}

