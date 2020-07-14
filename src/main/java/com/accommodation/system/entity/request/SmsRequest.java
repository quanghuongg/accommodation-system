package com.accommodation.system.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;


public class SmsRequest {
    @NotBlank
    private final String phoneNumber; // destination

    private final String code;

    public SmsRequest(@JsonProperty("phone_number") String phoneNumber,
                      @JsonProperty("code") String message) {
        this.phoneNumber = phoneNumber;
        this.code = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "SmsRequest{" +
                "phoneNumber= ..." + '\'' +
                ", message='" + code + '\'' +
                '}';
    }

}
