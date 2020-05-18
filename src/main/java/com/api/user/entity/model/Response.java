package com.api.user.entity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    @JsonProperty("code")
    private int code = 0;

    @JsonProperty("message")
    private String message = "success";

    @JsonProperty("data")
    private Object data;
}
