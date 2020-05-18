package com.accommodation.system.entity.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseRequest extends BaseModel {
    @JsonProperty(value = "request_id")
    private String requestId;

    //yyyyMMddhhmmss
    @JsonProperty(value = "request_date")
    private String requestDate;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
