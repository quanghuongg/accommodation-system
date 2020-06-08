package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 14 :45
 * To change this template use File | Settings | File and Code Templates.
 */
@Builder
@Setter
@Getter
public class ImagePost extends BaseModel {
    private int id;

    private int postId;

    private String path;

}
