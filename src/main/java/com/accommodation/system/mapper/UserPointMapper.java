package com.accommodation.system.mapper;

import com.accommodation.system.entity.UserPoint;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserPointMapper {

    @Select("SELECT * FROM user_point WHERE user_id =#{user_id}")
    @Results(id = "PointObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "point", property = "point"),
            @Result(column = "create_at", property = "createdAt"),
            @Result(column = "update_at", property = "updatedAt"),
    })
    UserPoint findByUserId(@Param("user_id") int userId);


    @Insert("insert into user_point(user_id,point,create_at,update_at) " +
            "values(#{userId},#{point},#{createdAt},#{updatedAt})")
    @ResultMap("PointObject")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void insertPoint(UserPoint userPoint);

    @Update("UPDATE user_point SET point = #{point}  WHERE user_id = #{user_id}")
    @ResultMap("PointObject")
    void updatePoint(@Param("user_id") int userId, @Param("point") int point);


}
