package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  18/06/2020
 * Time: 14 :08
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
public class RoomType extends BaseModel {
    private String id;

    private String name;

    private String description;
}
