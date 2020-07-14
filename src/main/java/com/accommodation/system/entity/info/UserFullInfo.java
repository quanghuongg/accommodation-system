package com.accommodation.system.entity.info;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 13 :46
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class UserFullInfo extends BaseModel {

    private long userId;

    private String username;

    private String email;

    private String displayName;

    private long createdAt;

    private long updatedAt;

    private String avatar;

    private int roleId;

    private String phone;
}
