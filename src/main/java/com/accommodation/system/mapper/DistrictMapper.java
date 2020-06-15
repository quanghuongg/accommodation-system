package com.accommodation.system.mapper;

import com.accommodation.system.entity.District;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DistrictMapper {
    @Select("SELECT * FROM district")
    List<District> listDistrict();

    @Select("SELECT * FROM district WHERE id =#{id} limit 1")
    District findDistrict(@Param("id") int id);
}
