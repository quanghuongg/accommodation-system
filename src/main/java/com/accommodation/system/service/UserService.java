package com.accommodation.system.service;

import com.accommodation.system.entity.Role;
import com.accommodation.system.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    int save(User user);

    void update(User user);

    User findByUsername(String username);

    User findByUserId(int userId);

    Role findRoleByUserId(int userId);

    User findByEmail(String email);

    boolean checkEmailExisted(String email);

    void addSkill(int id, List<Integer> skillIds);
}
