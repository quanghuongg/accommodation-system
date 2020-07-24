package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  24/07/2020
 * Time: 15 :51
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class UserPoint extends BaseModel {
    private int id;

    private int userId;

    private int point;

    private long createdAt;

    private long updatedAt;

}
