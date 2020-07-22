package com.accommodation.system.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface SocialService {
    String generateToken(String userSocial);

    String googleLoginNew(GoogleIdToken.Payload payload);
}
