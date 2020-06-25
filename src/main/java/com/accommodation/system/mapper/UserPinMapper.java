package com.accommodation.system.mapper;

import com.accommodation.system.entity.User;
import com.accommodation.system.entity.UserPin;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface UserPinMapper {
    @Select("SELECT * FROM user_pin WHERE user_id =#{userId}")
    @Results(id = "UserPinObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "post_id", property = "postId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "enable", property = "enable"),
    })
    List<UserPin> listUserPin(@RequestParam("userId") int userId);



    @Insert("insert into user_pin(user_id,post_id,created_at,updated_at,enable) " +
            "values(#{userId},#{postId},#{createdAt},#{updatedAt},#{enable})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("UserPinObject")
    void insertUserPin(UserPin userPin);


    @Delete("delete from user_pin WHERE user_id = #{userId} AND post_id = #{post_id}")
    void unUserPin(@Param("userId") int userId, @Param("post_id") String post_id);
}
