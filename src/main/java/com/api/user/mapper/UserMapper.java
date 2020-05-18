package com.api.user.mapper;

import com.api.user.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user")
    List<User> findUserAll();

    @Select("SELECT user.* FROM user,user_role WHERE user.id = user_role.user_id AND user_role.role_id =2")
    List<User> findTutorAll();

    @Select("SELECT * FROM user WHERE username = #{username} AND status=1 ")
    User findUserByName(String username);


    @Update("UPDATE user SET  password = #{password}, display_name =#{display_name}, phone =#{phone}, status = #{status} " +
            ", email =#{email}, address =#{address}, avatar =#{avatar} , hourly_wage =#{hourly_wage}, description =#{description} ,updated = #{updated} WHERE id = #{id}")
    void update(User user);


    @Insert("insert into user(username,display_name,password,status,phone,email,address,avatar,expired,created,updated) " +
            "values(#{username},#{display_name},#{password},#{status},#{phone},#{email},#{address},#{avatar},#{expired},#{created},#{updated})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void insertUser(User user);

    @Select("SELECT * FROM role WHERE name = #{name}")
    Role findRoleByName(String name);

    @Select("SELECT * FROM role WHERE id = #{id}")
    Role findRoleById(Integer id);

    @Delete("delete FROM user_role WHERE user_id = #{user_id}")
    void deleteUserRole(Integer user_id);

    @Select("SELECT  role.* FROM user_role , role WHERE user_id = #{user_id} and user_role.role_id = role.id limit 1 ")
    Role findRoleByUserId(Integer user_id);

    @Insert("insert into user_role(user_id,role_id) values(#{user_id},#{role_id})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void insertUserRole(UserRole userRole);

    @Select("SELECT * FROM user_role WHERE user_id = #{user_id} limit 1")
    UserRole getUserRole(Integer user_id);

    @Select("SELECT * FROM user WHERE id = #{userId}")
    User findByUserId(int userId);

    @Select("SELECT * FROM user WHERE email = #{email} limit 1")
    User findByEmail(String email);

    @Select("SELECT email FROM user")
    List<String> listEmail();

    @Insert("insert into user_skill(user_id,skill_id) values(#{user_id},#{skill_id})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void insertUserSkill(UserSkill userSkill);

    @Select("SELECT skill.* FROM user_skill, skill WHERE user_skill.user_id =#{userId} AND user_skill.skill_id = skill.id ")
    List<Skill> listSkillByUser(int userId);


    @Select("SELECT distinct user.* FROM user, user_skill WHERE user_skill.skill_id =#{skill_id} AND user_skill.user_id = user.id   ")
    List<User> findUserBySkillId(@Param("skill_id") int skill_id);

    @Select("SELECT  user.* FROM user WHERE user.display_name LIKE '%' #{username} '%'  AND  user.address LIKE '%' #{address} '%' ")
    List<User> findUserNameAndAddress(@Param("username") String username, @Param("address") String address);

    @Select("SELECT  user.* FROM user WHERE user.address LIKE '%' #{address} '%'  ")
    List<User> findUserAddress(@Param("address") String address);

    @Select("SELECT  user.* FROM user WHERE user.display_name LIKE '%' #{username} '%' ")
    List<User> findListUserByName(@Param("username") String username);
}
