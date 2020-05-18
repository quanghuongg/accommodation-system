package com.accommodation.system.entity.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse extends BaseModel {
    @JsonProperty(value = "request_id")
    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
