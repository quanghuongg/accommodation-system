package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.*;

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
@NoArgsConstructor
public class Post extends BaseModel {
    private String id;

    private String title;

    private int userId;

    private long price;

    private String location;

    private String description;

    private int roomTypeId;

    private Integer isVerified;

    private long createdAt;

    private long updatedAt;

    private int districtId;

    private int wardId;

    private int area;

    private String image;

    String[] images;

    int status;


}
