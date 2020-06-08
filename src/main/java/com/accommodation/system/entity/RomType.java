package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 14 :47
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class RomType extends BaseModel {
    private String id;

    private String name;

    private String description;

}
