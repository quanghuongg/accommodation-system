package com.accommodation.system.entity.info;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class PostFullInfo extends BaseModel {
    public PostFullInfo() {
    }

    private String id;

    private String title;

    private long price;

    private String location;

    private String description;

    private String roomType;

    private long createdAt;

    private String district;

    private String ward;

    private int area;

    private String usePost;

    private String phone;

    private String avatarUserPost;

    List<String> images = new LinkedList<>();
}
