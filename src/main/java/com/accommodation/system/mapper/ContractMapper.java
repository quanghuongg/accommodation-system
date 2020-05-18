package com.accommodation.system.mapper;

import com.accommodation.system.entity.Contract;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContractMapper {

    @Insert("insert into contract(student_id,tutor_id, skill,description,number_hour,date_from,date_to,created,updated,total,status) " +
            "values(#{student_id},#{tutor_id},#{skill},#{description},#{number_hour},#{date_from},#{date_to},#{created},#{updated},#{total},#{status})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void createContract(Contract contract);

    @Select("SELECT * FROM contract WHERE id = #{id}")
    Contract findById(Integer id);

    @Update("UPDATE contract SET  status = #{status}, updated =#{updated} WHERE id = #{id}")
    void update(Contract contract);

    @Select("SELECT * FROM contract WHERE student_id = #{id}")
    List<Contract> listContractByStudentId(Integer id);

    @Select("SELECT * FROM contract WHERE tutor_id = #{id}")
    List<Contract> listContractByTutorId(Integer id);

    @Select("SELECT * FROM contract WHERE tutor_id = #{id} AND status=2 AND updated >=#{date_from} AND updated <=#{date_to} ")
    List<Contract> listRevenueByTime(Integer id, long date_from, long date_to);

    @Select("SELECT * FROM contract WHERE tutor_id = #{id} AND status=2")
    List<Contract> listRevenues(Integer userId);

    @Select("SELECT * FROM contract")
    List<Contract> getListContract();

    @Select("SELECT * FROM contract WHERE created > #{dateFrom} AND created <= #{dateTo} AND status= #{statusContract}")
    List<Contract> getListContractByFilter(long dateFrom, long dateTo, int statusContract);

    @Select("SELECT * FROM contract WHERE skill = #{skillId} AND created > #{dateFrom} AND created <= #{dateTo} AND status= 2")
    List<Contract> getListContractDoneBySkillIdByTime(int skillId, long dateFrom, long dateTo);

    @Select("SELECT * FROM contract WHERE skill = #{skillId} AND status= 2")
    List<Contract> getListContractDoneBySkillId(int skillId);


    @Select("SELECT * FROM contract WHERE tutor_id = #{tutorId} AND created > #{dateFrom} AND created <= #{dateTo} AND status= 2")
    List<Contract> getListContractDoneByTutorIdByTime(int tutorId, long dateFrom, long dateTo);

    @Select("SELECT * FROM contract WHERE tutor_id = #{tutorId} AND status= 2")
    List<Contract> getListContractDoneByTutorId(int tutorId);

    @Select("SELECT SUM(total) FROM contract WHERE  status= 2")
    int getTotalContractDone(long dateFrom, long l);
}
