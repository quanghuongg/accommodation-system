package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 14 :44
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class Post extends BaseModel {
    private String id;

    private Integer userId;

    private String price;

    private String location;

    private String content;

    private long roomTypeId;

    private Integer isVerified;

    private long createdAt;

    private long updatedAt;
}
