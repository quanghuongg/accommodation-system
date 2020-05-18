package com.api.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    private Integer id;

    private Integer user_id;

    private Integer role_id;

    public UserRole(Integer userId, Integer roleId) {
        this.user_id = userId;
        this.role_id = roleId;
    }
}
