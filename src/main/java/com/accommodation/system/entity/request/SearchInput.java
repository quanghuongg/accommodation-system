package com.accommodation.system.entity.request;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 08 :58
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
public class SearchInput extends BaseModel {
    List<String> ids = new ArrayList<>();

    @Builder.Default
    private int order = 1;

    private long price;

    private String location;

    private int area;

    private int roomTypeId;

    private long createdAt;

    private int page;

    private int size;

    private int districtId;

    private int wardId;

    private int userId;

}
