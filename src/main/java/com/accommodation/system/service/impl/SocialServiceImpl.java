package com.accommodation.system.service.impl;

import com.accommodation.system.entity.NotificationSetting;
import com.accommodation.system.entity.User;
import com.accommodation.system.security.TokenProvider;
import com.accommodation.system.service.NotificationService;
import com.accommodation.system.service.SocialService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import com.accommodation.system.uitls.Utils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
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
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenProvider;



    @Override
    public String generateToken(String userSocial) {
        Authentication socialAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSocial, userSocial, Collections.emptyList()));
        return jwtTokenProvider.generateToken(socialAuthentication);
    }

    @Override
    public String googleLoginNew(GoogleIdToken.Payload payload) {
        String username = payload.getSubject();
        User user = userService.findByUsername(username);
        if (Utils.isEmpty(user)) {
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            User googleUser = new User();
            googleUser.setUsername(username);
            googleUser.setPassword(ServiceUtils.encodePassword(username));
            googleUser.setEmail(email);
            googleUser.setAvatar(pictureUrl);
            googleUser.setAddress(locale);
            googleUser.setDisplayName(name);
            userService.save(googleUser);
            log.info("Add new user google  success {}!", googleUser.getDisplayName());
            //add notification setting
            notificationService.createNotificationSetting(NotificationSetting.builder()
                    .createdAt(System.currentTimeMillis())
                    .enable(0)
                    .userId(googleUser.getId())
                    .build());

        }
        return username;
    }
}
