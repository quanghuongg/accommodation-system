package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  26/06/2020
 * Time: 14 :43
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
public class Comment extends BaseModel {
    public Comment() {
    }

    private int id;

    private String postId;

    private String content;

    private int userId;

    private long createdAt;

    private long updatedAt;
}
