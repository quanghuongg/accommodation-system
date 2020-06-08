package com.accommodation.system.mapper;

import com.accommodation.system.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT * FROM post")
    @Results(id = "PostObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "price", property = "price"),
            @Result(column = "location", property = "location"),
            @Result(column = "content", property = "content"),
            @Result(column = "room_type_id", property = "roomTypeId"),
            @Result(column = "is_verified", property = "isVerified"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "created_at", property = "createdAt"),
    })
    List<Post> findAllPost();
}
