package com.api.user.entity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestInfo {
    int page;

    int size;

    @JsonProperty("role_id")
    int roleId;

    String name;

    String address;

    @JsonProperty("sort_by")
    String sortBy;

    @JsonProperty("order_by")
    String orderBy;

    @JsonProperty("skill_ids")
    private List<Integer> skillIds;


    @JsonProperty("skill_id")
    private int skillId;


    @JsonProperty("date_from")
    private long dateFrom;

    @JsonProperty("date_to")
    private long dateTo;

    @JsonProperty("status_contract")
    private int statusContract;


}
