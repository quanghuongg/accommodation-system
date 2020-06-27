package com.accommodation.system.entity.request;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest extends BaseModel {

    private String postId;

    private String title;

    private long price;

    private String location;

    private String description;

    private int roomTypeId;

    private int districtId;

    private int wardId;

    private int area;
}
