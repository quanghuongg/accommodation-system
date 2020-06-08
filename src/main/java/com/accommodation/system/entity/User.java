package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseModel {
    private Integer id;

    private String username;

    private String displayName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private String address;

    private String phone;

    private String avatar;

    private long expiredAt;

    private int status;

    private long createdAt;

    private long updatedAt;

    private String description;

}
