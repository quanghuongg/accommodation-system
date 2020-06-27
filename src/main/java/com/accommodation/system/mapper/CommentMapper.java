package com.accommodation.system.mapper;

import com.accommodation.system.entity.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;



@Mapper
public interface CommentMapper {
    @Select("SELECT * FROM comment WHERE post_id = #{postId}")
    @Results(id = "CommentObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "post_id", property = "postId"),
            @Result(column = "content", property = "content"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "create_at", property = "createdAt"),
            @Result(column = "update_at", property = "updatedAt"),
    })
    List<Comment> listCommentByPostId(@Param("postId") String postId);


    @Insert("insert into comment(post_id,content,user_id,create_at,update_at) " +
            "values(#{postId},#{content},#{userId},#{createdAt},#{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("CommentObject")
    void insertComment(Comment comment);
}
