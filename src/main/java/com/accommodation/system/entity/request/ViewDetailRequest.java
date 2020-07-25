package com.accommodation.system.entity.request;

import com.accommodation.system.entity.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewDetailRequest  extends BaseModel {
    String location;
    String postId;
}
