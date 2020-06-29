package com.accommodation.system.mapper;

import com.accommodation.system.entity.Notifications;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NotificationsMapper {
    @Select("SELECT * FROM notifications WHERE user_id = #{userId} ORDER BY id DESC")
    @Results(id = "NotificationObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "post_id", property = "postId"),
            @Result(column = "info", property = "info"),
            @Result(column = "message", property = "message"),
            @Result(column = "read_at", property = "readAt"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
    })
    List<Notifications> listNotificationByUserId(@Param("userId") int userId);


    @Insert("insert into notifications(user_id,post_id,info,message,read_at,created_at,updated_at) " +
            "values(#{userId},#{postId},#{info},#{message},#{readAt},#{createdAt},#{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("NotificationObject")
    void addNotification(Notifications notifications);

    @Select("SELECT * FROM notifications WHERE user_id = #{userId} AND read_at = 0 ")
    @ResultMap("NotificationObject")
    List<Notifications> countNotificationUnread(@Param("userId") int userId);

    @Update("UPDATE notifications SET read_at = #{readAt} WHERE id = #{id}")
    @ResultMap("NotificationObject")
    void updateReadAt(@Param("id") int notificationId, @Param("readAt") long readAt);
}
