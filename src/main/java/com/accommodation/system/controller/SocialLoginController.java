package com.accommodation.system.controller;

import com.accommodation.system.config.GoogleTokenVerifier;
import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.PhoneCode;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.model.SocialAccountInfo;
import com.accommodation.system.entity.request.SmsRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.SmsSender;
import com.accommodation.system.service.SocialService;
import com.accommodation.system.uitls.Utils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@Api(tags = {"SocialLogin API"})

public class SocialLoginController {
    @Autowired
    SocialService socialService;

    @Autowired
    GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    SmsSender smsService;

    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody SocialAccountInfo socialAccountInfo) throws Exception {
        String username = "";
        try {
            final GoogleIdToken.Payload payload = googleTokenVerifier.verify(socialAccountInfo.getIdToken());
            username = socialService.googleLoginNew(payload);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        if (Utils.isEmpty(username)) {
            throw new ApiServiceException("Login fail because id_token invalid");
        }
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(socialService.generateToken(username))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping("/phone-code")
    public ResponseEntity<?> sendSms(@Valid @RequestBody SmsRequest smsRequest) {
        smsService.sendSms(smsRequest);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/phone-login")
    public ResponseEntity<?> socialLogin(@RequestBody SmsRequest smsRequest) throws ApiServiceException {
        if (Utils.isEmpty(smsRequest.getPhoneNumber()) || Utils.isEmpty(smsRequest.getCode())) {
            throw new ApiServiceException("code invalid");
        }
        PhoneCode phoneCode = smsService.find(smsRequest.getPhoneNumber(), Integer.parseInt(smsRequest.getCode()));
        if (phoneCode == null) {
            throw new ApiServiceException("code invalid");
        }
        if (System.currentTimeMillis() - phoneCode.getCreatedAt() > 5 * 60 * 1000) {
            throw new ApiServiceException(" expiration code");
        }
        String username = smsService.phoneLogin(smsRequest.getPhoneNumber());
        smsService.updateStatus(phoneCode.getId());
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(socialService.generateToken(username))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
