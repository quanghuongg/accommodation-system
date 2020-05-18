package com.accommodation.system.entity.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractRequest {

    @JsonProperty(value = "tutor_id")
    private int tutorId;

    @JsonProperty(value = "number_hour")
    private int numberHour;

    @JsonProperty(value = "date_from")
    private long dateFrom;

    @JsonProperty(value = "date_to")
    private long dateTo;

    @JsonProperty(value = "total")
    double total;

    private String description;

    private int skill;

}
