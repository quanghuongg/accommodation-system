package com.accommodation.system.entity.model;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RequestInfo extends BaseModel {
    int page;

    int size;

    int roleId;

    String name;

    String address;

    String sortBy;

    String orderBy;

    private long dateFrom;

    private long dateTo;

}
