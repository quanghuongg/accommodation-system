package com.accommodation.system.config;

import com.accommodation.system.exception.ApiServiceException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GoogleTokenVerifier {

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();
    private static final String CLIENT_ID = "649195066861-l3kdb6478u61317jvt0vrn9qdaspufjo.apps.googleusercontent.com";


    public Payload verify(String idTokenString)
            throws GeneralSecurityException, IOException, ApiServiceException {
        return GoogleTokenVerifier.verifyToken(idTokenString);
    }

    private static Payload verifyToken(String idTokenString)
            throws GeneralSecurityException, IOException, ApiServiceException {
        List<String> clientIds = new ArrayList<>();
        clientIds.add(CLIENT_ID);
        clientIds.add("649195066861-jko1msk8gr23dq6f1oh2qtkqfojqalds.apps.googleusercontent.com");
        clientIds.add("649195066861-vmuch8aoo3eaqhl2qrqpje5j08jpfsu2.apps.googleusercontent.com");

        final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.
                Builder(transport, jsonFactory)
                .setIssuers(Arrays.asList("https://accounts.google.com", "accounts.google.com"))
                .setAudience(clientIds)
                .build();

        System.out.println("validating:" + idTokenString);

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (IllegalArgumentException e) {
            throw new ApiServiceException("idToken is invalid");
        }

        if (idToken == null) {
            throw new ApiServiceException("idToken is invalid");
        }

        return idToken.getPayload();
    }
}
