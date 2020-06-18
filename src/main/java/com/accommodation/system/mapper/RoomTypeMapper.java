package com.accommodation.system.mapper;

import com.accommodation.system.entity.RoomType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomTypeMapper {
    @Select("SELECT * FROM room_type")
    List<RoomType> listRoomType();
}
