package com.accommodation.system.mapper;

import com.accommodation.system.entity.Feedback;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import java.util.List;

@Mapper
public interface FeedbackMapper {
    @Insert("insert into feedback(contract_id,content, type,created) " +
            "values(#{contract_id},#{content},#{type},#{created})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void addFeedback(Feedback feedback);


    @Select("SELECT * FROM feedback ORDER BY created DESC")
    List<Feedback> listFeedBacks();
}
