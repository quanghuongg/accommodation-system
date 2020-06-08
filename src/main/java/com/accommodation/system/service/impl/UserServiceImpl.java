package com.accommodation.system.service.impl;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Role;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.UserRole;
import com.accommodation.system.entity.info.UserFullInfo;
import com.accommodation.system.entity.request.RegisterRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import com.accommodation.system.utils2.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {


    private UserMapper userMapper;


    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User [" + username + "] not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), Collections.singleton(getAuthorities(user)));
    }

    private SimpleGrantedAuthority getAuthorities(User user) {
        return (new SimpleGrantedAuthority("ROLE_" + userMapper.findRoleByUserId(user.getId()).getName()));
    }

    @Override
    public List<User> getAll() {
        return userMapper.findUserAll();
    }


    @Override
    public int save(RegisterRequest registerInfo) {
        User user = User.builder()
                .username(registerInfo.getUsername())
                .address(registerInfo.getAddress())
                .avatar(registerInfo.getAvatar())
                .createdAt(System.currentTimeMillis())
                .description(registerInfo.getDescription())
                .displayName(registerInfo.getDisplayName())
                .email(registerInfo.getEmail())
                .phone(registerInfo.getPhone())
                .status(0)
                .build();

        user.setPassword(ServiceUtils.encodePassword(registerInfo.getPassword()));
        userMapper.insertUser(user);
        Role role = userMapper.findRoleById(registerInfo.getRoleId());
        UserRole userRole = new UserRole(user.getId(), role.getId());
        userMapper.insertUserRole(userRole);
        log.info("Create user {} success!", user.getUsername());
        return user.getId();
    }

    @Override
    public void update(User existedUser, User user) throws ApiServiceException {
        if (ServiceUtils.isNotEmpty(user.getDisplayName())) {
            existedUser.setDisplayName(user.getDisplayName());
        }
        if (ServiceUtils.isNotEmpty(user.getEmail())) {
            if (!ServiceUtils.isValidMail(user.getEmail())) {
                throw new ApiServiceException("email invalid");
            }
            existedUser.setEmail(user.getEmail());
        }
        if (ServiceUtils.isNotEmpty(user.getPassword())) {
            existedUser.setPassword(ServiceUtils.encodePassword(user.getPassword()));
        }
        if (ServiceUtils.isNotEmpty(user.getPhone())) {
            if (!ServiceUtils.isValidPhone(user.getPhone())) {
                throw new ApiServiceException("phone invalid");
            }
            existedUser.setPhone(user.getPhone());
        }
        if (ServiceUtils.isNotEmpty(user.getAddress())) {
            existedUser.setAddress(user.getAddress());
        }
        if (ServiceUtils.isNotEmpty(user.getAvatar())) {
            existedUser.setAvatar(user.getAvatar());
        }
        if (ServiceUtils.isNotEmpty(user.getStatus()) && user.getStatus() != 0) {
            existedUser.setStatus(user.getStatus());
        }
        if (ServiceUtils.isNotEmpty(user.getDescription())) {
            existedUser.setDescription(user.getDescription());
        }
        user.setUpdatedAt(System.currentTimeMillis());
        userMapper.update(existedUser);
    }

    @Override
    public void update(User user) {
        user.setUpdatedAt(System.currentTimeMillis());
        userMapper.update(user);
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findUserByName(username);
        return user;
    }

    @Override
    public UserFullInfo findByName(String username) throws ApiServiceException {
        User user = userMapper.findUserByName(username);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("User not existed");
        }
        UserFullInfo userFullInfo = UserFullInfo.builder()
                .displayName(user.getDisplayName())
                .userId(user.getId())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .username(user.getUsername())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
        Role role = findRoleByUserId(user.getId());
        userFullInfo.setRoleId(role.getId());
        return userFullInfo;
    }

    @Override
    public User findByUserId(int userId) {
        User user = userMapper.findByUserId(userId);
        return user;
    }

    @Override
    public Role findRoleByUserId(int userId) {
        UserRole userRole = userMapper.getUserRole(userId);
        return userMapper.findRoleById(userRole.getRole_id());
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public boolean checkEmailExisted(String email) {
        List<String> list = userMapper.listEmail();
        return list.contains(email);
    }

    @Override
    public String uploadAvatar(int userId, MultipartFile file) throws ApiServiceException {
        if (Utils.isEmpty(file) || file.isEmpty()) {
            throw new ApiServiceException("File not found");
        }
        User currentUser = userMapper.findByUserId(userId);
        if (Utils.isEmpty(currentUser)) {
            throw new ApiServiceException("user not found");
        }
        try {
            //Build Path
            String fileName = DigestUtils.sha1Hex(String.valueOf(userId)) + Constant.FileUploader.MediaType.IMAGE_EXTENSION;
            String strPath = Constant.STORAGE_PATH + Constant.FileUploader.PATH_UPLOAD + Constant.FileUploader.PATH_AVATARS;
            log.info("[Service] [upload avatar] userId = {}, fileName = {}, strPath = {}", userId, fileName, strPath);
            //Create directory
            Path path = Paths.get(strPath);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    //fail to create directory
                    e.printStackTrace();
                }
            }
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path pathFile = Paths.get(strPath + "/" + fileName);
            Files.write(pathFile, bytes);

            String avatarPathToSave = Constant.FileUploader.PATH_UPLOAD + Constant.FileUploader.PATH_AVATARS + "/" + fileName;
            log.info("[Service] [upload avatar] avatarPath = {}", avatarPathToSave);
            // update mysql
            currentUser.setAvatar(avatarPathToSave);
            userMapper.update(currentUser);
            return Constant.FileUploader.PATH_CDN + avatarPathToSave;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("[Service] [upload avatar] cannot save avatar file (exception)", e);
            throw new ApiServiceException("[Service] [upload avatar] cannot save avatar file (exception)");
        }
    }
}
