package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.*;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 14 :42
 * To change this template use File | Settings | File and Code Templates.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSetting extends BaseModel {
    private int id;

    private Integer userId;

    private int wardId;

    private int districtId;

    private String location;

    private int roomTypeId;

    private int minArea;

    private int maxArea;

    private long minPrice;

    private long maxPrice;

    private long createdAt;

    private long updatedAt;

    private Integer enable;

}
