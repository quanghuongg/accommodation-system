package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends BaseModel {

    private Integer id;

    private Integer user_id;

    private Integer role_id;

    public UserRole(Integer user_id, Integer role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }
}
