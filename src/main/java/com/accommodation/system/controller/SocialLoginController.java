package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.model.SocialAccountInfo;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.SocialService;
import com.accommodation.system.uitls.ServiceUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = {"SocialLogin API"})
public class SocialLoginController {
    @Autowired
    SocialService socialService;

    @PostMapping("/social-login")
    public ResponseEntity<?> socialLogin(@RequestBody SocialAccountInfo socialAccountInfo) throws Exception, ApiServiceException {
        socialService.checkValidInput(socialAccountInfo);
        try {
            String userSocial = "";
            String loginType = socialAccountInfo.getType();
            if (loginType.equals("facebook")) {
                userSocial = socialService.facebookLogin(socialAccountInfo);
            } else {
                userSocial = socialService.googleLogin(socialAccountInfo);
            }
            if (ServiceUtils.isNotEmpty(userSocial)) {
                String token = socialService.generateToken(userSocial);
                Response response = Response.builder()
                        .code(Constant.SUCCESS_CODE)
                        .message(Constant.SUCCESS_MESSAGE)
                        .data(token)
                        .build();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            throw new ApiServiceException("Login fail");
        } catch (Exception e) {
            log.error("Login social fail:  not into try action!");
            e.printStackTrace();
            throw new ApiServiceException("Login fail because id_token invalid");
        }
    }
}
