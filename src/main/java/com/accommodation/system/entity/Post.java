package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Post extends BaseModel {
    private int id;

    private int userId;

    private long price;

    private String location;

    private String description;

    private long roomTypeId;

    private Integer isVerified;

    private long createdAt;

    private long updatedAt;
}