package com.accommodation.system.service;

import com.accommodation.system.entity.PhoneCode;
import com.accommodation.system.entity.request.SmsRequest;

public interface SmsSender {

    void sendSms(SmsRequest smsRequest);

    void addPhoneCode(String phone, int code);

    PhoneCode find(String phone, int code);

    String phoneLogin(String phone);

    void updateStatus(int id);
}
