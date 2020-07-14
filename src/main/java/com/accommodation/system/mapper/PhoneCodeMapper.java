package com.accommodation.system.mapper;

import com.accommodation.system.entity.PhoneCode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PhoneCodeMapper {
    @Select("SELECT * FROM phone_code WHERE phone = #{phone} AND code = #{code} AND enable = 0 limit 1")
    @Results(id = "CodeObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "phone", property = "phone"),
            @Result(column = "code", property = "code"),
            @Result(column = "enable", property = "enable"),
            @Result(column = "create_at", property = "createdAt"),
            @Result(column = "update_at", property = "updatedAt"),
    })
    PhoneCode findByPhone(@Param("phone") String phone, @Param("code") int code);


    @Insert("insert into phone_code(phone,code,enable,create_at,update_at) " +
            "values(#{phone},#{code},#{enable},#{createdAt},#{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("CodeObject")
    void insertCode(PhoneCode phoneCode);

    @Update("UPDATE phone_code SET enable = 1  WHERE id = #{id}")
    @ResultMap("CodeObject")
    void updateStatus(@Param("id") int id);
}
