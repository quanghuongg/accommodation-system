package com.accommodation.system.mapper;

import com.accommodation.system.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    @Select("SELECT * FROM post")
    @Results(id = "PostObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "price", property = "price"),
            @Result(column = "location", property = "location"),
            @Result(column = "room_type_id", property = "roomTypeId"),
            @Result(column = "description", property = "description"),
            @Result(column = "is_verified", property = "isVerified"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "created_at", property = "createdAt"),
    })
    List<Post> findAllPost();


    @Insert("insert into post(user_id,price,location,content,room_type_id,is_verified,updated_at,created_at) " +
            "values(#{userId},#{price},#{location},#{content},#{roomTypeId},#{isVerified},#{updatedAt},#{createdAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("PostObject")
    void insertPost(Post post);

    @Select("SELECT * FROM post WHERE user_id = #{userId} AND id = #{postId}")
    @ResultMap("PostObject")
    Post findPost(@Param("userId") int userId, @Param("postId") int postId);
}
