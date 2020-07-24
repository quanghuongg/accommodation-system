package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  24/07/2020
 * Time: 15 :47
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
public class Feedback extends BaseModel {
    private int id;

    private String postId;

    private int userFeedBackId;

    private int userPostId;

    private String content;

    private long createdAt;

    private long updatedAt;

    public Feedback() {
    }
}
