package com.accommodation.system.service.impl;

import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.SocialAccountInfo;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.security.TokenProvider;
import com.accommodation.system.service.SocialService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Slf4j
@Service(value = "socialService")
public class SocialServiceImpl implements SocialService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenProvider;

    @Override
    public String facebookLogin(SocialAccountInfo socialAccountInfo) throws ApiServiceException {
//        User facebookUser = null;
//        try {
//            FacebookClient facebookClient = new DefaultFacebookClient(socialAccountInfo.getIdToken(), Version.VERSION_4_0);
//            facebookUser = facebookClient.fetchObject("me", User.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ApiServiceException("Login fail");
//        }
//
//        if (facebookUser == null) {
//            log.error("Login facebook fail because facebookUser null!");
//            throw new ApiServiceException("Login fail");
//        }
//
//        String facebookUserId = facebookUser.getId();
//        UserInfo currentUser = (UserInfo) userService.findByUsername(facebookUserId);
//        if (currentUser == null) {
//            log.info("Add new user facebook!");
//            currentUser = new UserInfo();
//            currentUser.setFacebookUid(facebookUserId);
//            currentUser.setPassword(facebookUserId);
//            currentUser.setRoleId(socialAccountInfo.getRoleId());
//            currentUser.setFullName(facebookUser.getName());
////            int userId = userService.save(currentUser);
//            int userId =0;
//            if (userId == 0 || String.valueOf(userId).equals("null")) {
//                log.error("Login by social facebook fail because add new user fail!");
//                throw new ApiServiceException("Login fail");
//            }
//            log.info("Add new user facebook  success {}!", currentUser.getFullName());
//        }
//        return currentUser.getFacebookUid();
        return null;
    }


    @Override
    public String googleLogin(SocialAccountInfo socialAccountInfo) throws FirebaseAuthException, ApiServiceException {
        UserRecord firebaseUser = ServiceUtils.getUserRecordByFirebaseIdToken(socialAccountInfo.getIdToken());
        if (firebaseUser == null) {
            log.error("Id_token invalid !");
            throw new ApiServiceException("Login fail");
        }
        com.google.firebase.auth.UserInfo firebaseUserInfo = firebaseUser.getProviderData()[0];
        String googleUid = firebaseUserInfo.getUid();
        User googleUser = userService.findByUsername(googleUid);
        if (googleUser == null) {
            log.info("Add new user google!");
            googleUser = new User();
            googleUser.setUsername(googleUid);
            googleUser.setPassword(ServiceUtils.encodePassword(googleUid));
            googleUser.setEmail(firebaseUserInfo.getEmail());
            googleUser.setDisplayName(firebaseUserInfo.getDisplayName());
            googleUser.setAvatar(firebaseUserInfo.getPhotoUrl());
            googleUser.setPhone(firebaseUserInfo.getPhoneNumber());
            userService.save(googleUser);
            log.info("Add new user google  success {}!", googleUser.getDisplayName());
        }
        return googleUser.getUsername();
    }

    @Override
    public void checkValidInput(SocialAccountInfo socialAccountInfo) throws ApiServiceException {
        if (ServiceUtils.isEmpty(socialAccountInfo) || ServiceUtils.isEmpty(socialAccountInfo.getIdToken()) || ServiceUtils.isEmpty(socialAccountInfo.getIdToken())) {
            throw new ApiServiceException("Empty field");
        }
        String loginType = socialAccountInfo.getType();
        if (!loginType.equals("facebook") && !loginType.equals("google") || socialAccountInfo.getIdToken() == null) {
            log.error("Login social fail because without login type social!");
            throw new ApiServiceException("Login type social invalid");
        }
    }

    @Override
    public String generateToken(String userSocial) {
        Authentication socialAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSocial, userSocial, Collections.emptyList()));
        return jwtTokenProvider.generateToken(socialAuthentication);
    }
}
