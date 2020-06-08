package com.accommodation.system.mapper;

import com.accommodation.system.entity.Role;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.UserRole;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user")
    @Results(id = "UserObject", value = {
            @Result(column = "id", property = "id", id = true),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "display_name", property = "displayName"),
            @Result(column = "status", property = "status"),
            @Result(column = "phone", property = "phone"),
            @Result(column = "email", property = "email"),
            @Result(column = "address", property = "address"),
            @Result(column = "avatar", property = "avatar"),
            @Result(column = "expired_at", property = "expiredAt"),
            @Result(column = "updated_at", property = "updatedAt"),
            @Result(column = "created_at", property = "createdAt"),
            @Result(column = "description", property = "description"),
    })
    List<User> findUserAll();

    @Select("SELECT * FROM user WHERE username = #{username} AND status=1 ")
    @ResultMap("UserObject")
    User findUserByName(String username);

    @Update("UPDATE user SET  password = #{password}, display_name =#{displayName}, phone =#{phone}, status = #{status} " +
            ", email =#{email}, address =#{address}, avatar =#{avatar} , description =#{description} ,updated_at = #{updatedAt} WHERE id = #{id}")
    @ResultMap("UserObject")
    void update(User user);


    @Insert("insert into user(username,display_name,password,status,phone,email,address,avatar,expired_at,created_at,updated_at) " +
            "values(#{username},#{displayName},#{password},#{status},#{phone},#{email},#{address},#{avatar},#{expiredAt},#{createdAt},#{updatedAt})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    @ResultMap("UserObject")
    void insertUser(User user);


    @Select("SELECT * FROM role WHERE id = #{id}")
    Role findRoleById(Integer id);

    @Select("SELECT  role.* FROM user_role , role WHERE user_id = #{user_id} and user_role.role_id = role.id limit 1 ")
    Role findRoleByUserId(Integer user_id);

    @Insert("insert into user_role(user_id,role_id) values(#{user_id},#{role_id})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id",
            before = false, resultType = Integer.class)
    void insertUserRole(UserRole userRole);


    @Select("SELECT * FROM user_role WHERE user_id = #{user_id} limit 1")
    UserRole getUserRole(Integer user_id);


    @Select("SELECT * FROM user WHERE id = #{userId}")
    @ResultMap("UserObject")
    User findByUserId(int userId);

    @Select("SELECT * FROM user WHERE email = #{email} limit 1")
    @ResultMap("UserObject")
    User findByEmail(String email);

    @Select("SELECT email FROM user")
    List<String> listEmail();

}
