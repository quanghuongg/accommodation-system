package com.accommodation.system.entity;

import com.accommodation.system.entity.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 14 :44
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Post extends BaseModel {
    private String id;

    private int userId;

    private long price;

    private String location;

    private String description;

    private long roomTypeId;

    private Integer isVerified;

    private long createdAt;

    private long updatedAt;

    private int districtId;

    private int wardId;

    private int area;

    List<File> images = new LinkedList<>();


}
