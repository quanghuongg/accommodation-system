package com.accommodation.system.entity.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private long price;

    private String location;

    private String description;

    private long roomTypeId;
}
