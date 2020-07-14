package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.*;

/**
 * User: huongnq4
 * Date:  14/07/2020
 * Time: 09 :25
 * To change this template use File | Settings | File and Code Templates.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCode extends BaseModel {
    private int id;

    private String phone;

    private int code;

    private int enable;

    private long createdAt;

    private long updatedAt;
}
