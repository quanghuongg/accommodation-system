package com.accommodation.system.mapper;

import com.accommodation.system.entity.Skill;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ManageMapper {
    @Select("select * from skill")
    List<Skill> listAllSkill();

    @Select("select * from skill WHERE id = #{id} AND status=1 ")
    Skill findSkillById(int id);

    @Insert("insert into skill(name,description,created,updated,status) " +
            "values(#{name},#{description},#{created},#{updated},#{status})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void insertSkill(Skill skill);

    @Update("UPDATE skill SET  name = #{name}, description =#{description}, status = #{status},updated = #{updated} WHERE id = #{id}")
    void updateSkill(Skill skill);
}
