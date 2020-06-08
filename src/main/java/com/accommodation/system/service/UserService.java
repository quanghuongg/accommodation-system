package com.accommodation.system.service;

import com.accommodation.system.entity.Role;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.request.RegisterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> getAll();

    int save(RegisterRequest user);

    void update(User user);

    User findByUsername(String username);

    User findByUserId(int userId);

    Role findRoleByUserId(int userId);

    User findByEmail(String email);

    boolean checkEmailExisted(String email);

    String uploadAvatar(int userId, MultipartFile file);
}
