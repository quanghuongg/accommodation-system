package com.api.user.service;

import com.api.user.entity.model.SocialAccountInfo;
import com.api.user.exception.ApiServiceException;
import com.google.firebase.auth.FirebaseAuthException;

public interface SocialService {
    String googleLogin(SocialAccountInfo socialAccountInfo) throws ApiServiceException, FirebaseAuthException;

    String facebookLogin(SocialAccountInfo socialAccountInfo) throws ApiServiceException;

    void checkValidInput(SocialAccountInfo socialAccountInfo) throws ApiServiceException;

    String generateToken(String userSocial);
}
