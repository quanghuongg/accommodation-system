package com.accommodation.system.entity.info;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MyPost extends BaseModel {
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

    String[] images ;
}
