package com.accommodation.system.entity.info;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentInfo extends BaseModel {
    private String content;

    private long createdAt;

    private String userComment;

    private String avatarUerComment;

    public CommentInfo() {
    }
}
