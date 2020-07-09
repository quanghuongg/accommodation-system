package com.accommodation.system.service;

import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.entity.model.SocialAccountInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.firebase.auth.FirebaseAuthException;

public interface SocialService {
    String googleLogin(SocialAccountInfo socialAccountInfo) throws ApiServiceException, FirebaseAuthException;

    String facebookLogin(SocialAccountInfo socialAccountInfo) throws ApiServiceException;

    void checkValidInput(SocialAccountInfo socialAccountInfo) throws ApiServiceException;

    String generateToken(String userSocial);

    String googleLoginNew(GoogleIdToken.Payload payload);
}
