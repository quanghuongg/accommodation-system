package com.accommodation.system.service.impl;

import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.SocialAccountInfo;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.security.TokenProvider;
import com.accommodation.system.service.SocialService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import com.accommodation.system.uitls.Utils;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
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
        User facebookUser = null;
        try {
            FacebookClient facebookClient = new DefaultFacebookClient(socialAccountInfo.getIdToken(), Version.VERSION_4_0);
            facebookUser = facebookClient.fetchObject("me", User.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiServiceException("Login fail");
        }
        if (facebookUser == null) {
            throw new ApiServiceException("Login fail");
        }
        String facebookUserId = facebookUser.getUsername();
        User fbUser = userService.findByUsername(facebookUserId);
        if (Utils.isEmpty(fbUser)) {
            fbUser = new User();
            fbUser.setUsername(facebookUserId);
            fbUser.setPassword(ServiceUtils.encodePassword(facebookUserId));
            fbUser.setDisplayName(facebookUser.getDisplayName());
            userService.save(fbUser);
        }
        return fbUser.getUsername();
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
            throw new ApiServiceException("Login type social invalid");
        }
    }


    @Override
    public String phoneLogin(String firebaseIdToken) throws ApiServiceException {
        FirebaseToken firebaseToken = ServiceUtils.decodeFirebaseIdToken(firebaseIdToken);
        if (firebaseToken == null) {
            throw new ApiServiceException(" Can't get firebaseUser!");
        }
        String phoneString = firebaseToken.getClaims().get("phone_number").toString();
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        long phoneNumber = 0;
        try {
            Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneString, "");
            phoneNumber = numberProto.getNationalNumber();
        } catch (Exception e) {
            throw new ApiServiceException("Can't get country code!");
        }
        User user = userService.findByUsername(String.valueOf(phoneNumber));
        if (Utils.isEmpty(user)) {
            User userAdd = new User();
            userAdd.setPhone(phoneString);
            userAdd.setPassword(ServiceUtils.encodePassword(String.valueOf(phoneNumber)));
            userAdd.setDisplayName(phoneString);
            userService.save(userAdd);
        }
        return String.valueOf(phoneNumber);
    }

    @Override
    public String generateToken(String userSocial) {
        Authentication socialAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSocial, userSocial, Collections.emptyList()));
        return jwtTokenProvider.generateToken(socialAuthentication);
    }
}
