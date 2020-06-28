package com.accommodation.system.mapper;

import com.accommodation.system.entity.NotificationSetting;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationSettingMapper {
    @Select("SELECT * FROM notification_setting WHERE district_id =#{district_id} AND enable =1")
    @Results(id = "NotificationSettingObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "price", property = "price"),
            @Result(column = "ward_id", property = "wardId"),
            @Result(column = "district_id", property = "districtId"),
            @Result(column = "area", property = "area"),
            @Result(column = "location", property = "location"),
            @Result(column = "room_type_id", property = "roomTypeId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "enable", property = "enable"),
    })
    List<NotificationSetting> findNotificationSetting(int district_id);


    @Insert("insert into notification_setting(user_id,price,ward_id,district_id,area,location,room_type_id,created_at,updated_at,enable) " +
            "values(#{userId},#{price},#{wardId},#{districtId},#{area},#{location},#{roomTypeId},#{createdAt},#{updatedAt},#{enable})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("NotificationSettingObject")
    void createNotificationSetting(NotificationSetting notificationSetting);


    @Update("UPDATE notification_setting SET  price = #{price}, ward_id =#{wardId}, district_id =#{districtId}, area =#{area},location = #{location},enable = #{enable} " +
            ", room_type_id =#{roomTypeId}, created_at =#{createdAt}, updated_at =#{updatedAt} WHERE id = #{id}")
    @ResultMap("NotificationSettingObject")
    void updateNotificationSetting(NotificationSetting notificationSetting);


    @Select("SELECT * FROM notification_setting WHERE user_id =#{user_id}")
    @ResultMap("NotificationSettingObject")
    NotificationSetting findByUser(int user_id);
}
