package com.accommodation.system.mapper;

import com.accommodation.system.entity.NotificationSetting;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationSettingMapper {
    @Select("SELECT * FROM notification_setting")
    @Results(id = "NotificationSettingObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "price", property = "price"),
            @Result(column = "ward_id", property = "wardId"),
            @Result(column = "district_id", property = "districtId"),
            @Result(column = "location", property = "location"),
            @Result(column = "room_type_id", property = "roomTypeId"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "updated_at", property = "updatedAt"),
    })
    List<NotificationSetting> findAllNotificationSetting();


    @Insert("insert into post(user_id,price,ward_id,district_id,location,room_type_id,created_at,updated_at) " +
            "values(#{userId},#{price},#{wardId},#{districtId},#{location},#{roomTypeId},#{createdAt},#{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("NotificationSettingObject")
    void createNotificationSetting(NotificationSetting notificationSetting);


    @Update("UPDATE user SET  price = #{price}, ward_id =#{wardId}, district_id =#{districtId}, location = #{location} " +
            ", room_type_id =#{roomTypeId}, created_at =#{createdAt}, updated_at =#{updated_at} WHERE id = #{id}")
    @ResultMap("UserObject")
    void updateNotificationSetting(NotificationSetting notificationSetting);
}
