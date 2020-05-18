package com.api.user.entity.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackRequest {
    @JsonProperty(value = "contract_id")
    private int contractId;

    private String content;

    private int type;
}
