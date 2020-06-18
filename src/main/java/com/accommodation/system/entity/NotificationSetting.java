package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 14 :42
 * To change this template use File | Settings | File and Code Templates.
 */
@Builder
@Setter
@Getter
public class NotificationSetting extends BaseModel {
    private int id;

    private Integer userId;

    private long price;

    private int wardId;

    private int districtId;

    private int area;

    private String location;

    private long roomTypeId;

    private long createdAt;

    private long updatedAt;

}
