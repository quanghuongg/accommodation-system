package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.define.ContextPath;
import com.accommodation.system.entity.info.PhoneAccountInfo;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.SocialService;
import com.accommodation.system.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = {"PhoneLogin API"})
public class PhoneLoginController {
    @Autowired
    UserService userService;

    @Autowired
    SocialService socialService;

    @PostMapping(ContextPath.Login.PHONE_LOGIN)
    public ResponseEntity<?> phoneLogin(PhoneAccountInfo phoneAccountInfo) throws Exception {
        String firebaseIdToken = phoneAccountInfo.getIdToken();
        if (firebaseIdToken == null) {
            throw new ApiServiceException("Missing id token!");
        }
        String phone = socialService.phoneLogin(firebaseIdToken);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(socialService.generateToken(phone))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
