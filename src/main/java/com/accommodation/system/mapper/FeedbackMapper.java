package com.accommodation.system.mapper;

import com.accommodation.system.entity.Feedback;
import com.accommodation.system.entity.UserPoint;
import org.apache.ibatis.annotations.*;


@Mapper
public interface FeedbackMapper {


    @Select("SELECT * FROM feedback WHERE id =#{id}")
    @Results(id = "FbObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "post_id", property = "postId"),
            @Result(column = "user_feedback_id", property = "userFeedBackId"),
            @Result(column = "user_post_id", property = "userPostId"),
            @Result(column = "content", property = "content"),
            @Result(column = "create_at", property = "createdAt"),
            @Result(column = "update_at", property = "updatedAt"),
    })
    UserPoint findById(@Param("id") int id);



    @Insert("insert into feedback(post_id,user_feedback_id,user_post_id,content,create_at,update_at) " +
            "values(#{postId},#{userFeedBackId},#{userPostId},#{content},#{createdAt},#{updatedAt})")
    @ResultMap("FbObject")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void insertFeedback(Feedback feedback);


}
