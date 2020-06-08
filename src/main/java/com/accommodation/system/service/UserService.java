package com.accommodation.system.service;

import com.accommodation.system.entity.Role;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.info.UserFullInfo;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.RegisterRequest;
import com.accommodation.system.exception.ApiServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> getAll();

    int save(RegisterRequest user);

    void update(User existedUser, User user) throws ApiServiceException;

    void update(User user) throws ApiServiceException;

    User findByUsername(String username);

    UserFullInfo findByName(String username) throws ApiServiceException;

    User findByUserId(int userId);

    Role findRoleByUserId(int userId);

    User findByEmail(String email);

    boolean checkEmailExisted(String email);

    String uploadAvatar(int userId, MultipartFile file) throws ApiServiceException;

}
