package com.accommodation.system.entity.info;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  25/06/2020
 * Time: 09 :56
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class NotificationSettingInfo extends BaseModel {
    private int id;

    private long price;

    private String ward;

    private String district;

    private int area;

    private String location;

    private String roomType;

    private  int enable;

}
