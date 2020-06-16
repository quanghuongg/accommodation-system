package com.accommodation.system.entity.request;

import com.accommodation.system.entity.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 11 :35
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
public class RegisterRequest extends BaseModel {
    private String username;

    private String displayName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    private String password;

    private String email;

    private String address;

    private String phone;

    private String avatar;

    private int status;

    private int roleId;

    private String description;
}
