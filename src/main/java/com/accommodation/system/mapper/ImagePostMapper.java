package com.accommodation.system.mapper;

import com.accommodation.system.entity.ImagePost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ImagePostMapper {
    @Select("SELECT * FROM image_post WHERE post_id = #{postId}")
    @Results(id = "ImagePostObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "post_id", property = "postId"),
            @Result(column = "path", property = "path"),
    })
    List<ImagePost> listImageByPostId(@Param("postId") int postId);
}
