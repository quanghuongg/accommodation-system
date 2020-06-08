package com.accommodation.system.mapper;

import com.accommodation.system.entity.Notifications;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NotificationsMapper {
    @Select("SELECT * FROM notifications WHERE user_id = #{userId}")
    @Results(id = "NotificationObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "post_id", property = "postId"),
            @Result(column = "info", property = "info"),
            @Result(column = "message", property = "message"),
            @Result(column = "read_at", property = "readAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "created_at", property = "createdAt"),
    })
    List<Notifications> listNotificationByUserId(@Param("userId") int userId);
}
