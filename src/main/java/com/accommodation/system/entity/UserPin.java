package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 11 :03
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class UserPin extends BaseModel {
    private int id;

    private int userId;

    String postId;

    private long createdAt;

    private long updatedAt;

    private int enable;

}
