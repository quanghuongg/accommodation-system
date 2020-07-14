package com.accommodation.system.service.impl;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.PhoneCode;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.request.SmsRequest;
import com.accommodation.system.mapper.PhoneCodeMapper;
import com.accommodation.system.service.SmsSender;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import com.accommodation.system.uitls.Utils;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service("twilio")
@Slf4j
public class TwilioSmsSender implements SmsSender {


    @Autowired
    PhoneCodeMapper codeMapper;

    @Autowired
    UserService userService;

    @Override
    public void sendSms(SmsRequest smsRequest) {
        if (isPhoneNumberValid(smsRequest.getPhoneNumber())) {
            Random rand = new Random();
            int code = rand.nextInt(999999 - 100000 + 1) + 100000;
            PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
            PhoneNumber from = new PhoneNumber("+12513129651");
            String message = "CODE: " + code;
            MessageCreator creator = Message.creator(to, from, message);
            creator.create();
            log.info("Send sms {}", smsRequest);
            addPhoneCode(smsRequest.getPhoneNumber(), code);
        } else {
            throw new IllegalArgumentException(
                    "Phone number [" + smsRequest.getPhoneNumber() + "] is not a valid number"
            );
        }

    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        // TODO: Implement phone number validator
        return true;
    }

    @Override
    public void addPhoneCode(String phone, int code) {
        PhoneCode phoneCode = PhoneCode.builder()
                .code(code)
                .phone(phone)
                .createdAt(System.currentTimeMillis())
                .enable(0)
                .build();
        codeMapper.insertCode(phoneCode);
    }

    @Override
    public PhoneCode find(String phone, int code) {
        return codeMapper.findByPhone(phone, code);
    }

    @Override
    public String phoneLogin(String phone) {
        String username = phone;
        User user = userService.findByUsername(phone);
        if (Utils.isEmpty(user)) {
            User phoneUser = new User();
            phoneUser.setUsername(username);
            phoneUser.setPassword(ServiceUtils.encodePassword(username));
            phoneUser.setAvatar(Constant.DEFAULT_AVATAR);
            phoneUser.setDisplayName(username);
            phoneUser.setPhone(phone);
            userService.save(phoneUser);
            log.info("Add new user phone  success {}!", phoneUser.getDisplayName());
        }
        return username;
    }

    @Override
    public void updateStatus(int id) {
        codeMapper.updateStatus(id);
    }
}
