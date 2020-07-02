package com.accommodation.system.mapper;

import com.accommodation.system.entity.Ward;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 10 :32
 * To change this template use File | Settings | File and Code Templates.
 */
@Mapper
public interface WardMapper {

    @Select("SELECT * FROM ward WHERE district_id =#{districtId}")
    @Results(id = "WardObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "district_id", property = "districtId"),
            @Result(column = "name", property = "name"),
    })
    List<Ward> listWard(@Param("districtId") int districtId);

    @Select("SELECT * FROM ward WHERE id =#{id}")
    @ResultMap("WardObject")
    Ward findWard(@Param("id") int id);

    @Select("SELECT * FROM ward WHERE name =#{name} AND  district_id =#{district_id} limit 1")
    @ResultMap("WardObject")
    Ward findWardByName(@Param("name") String name, @Param("district_id") int district_id);
}
