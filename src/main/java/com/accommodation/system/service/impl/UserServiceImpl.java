package com.accommodation.system.service.impl;

import com.accommodation.system.entity.*;
import com.accommodation.system.entity.request.RegisterRequest;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
                .address(registerInfo.getAddress())
                .avatar(registerInfo.getAvatar())
                .created(System.currentTimeMillis())
                .description(registerInfo.getDescription())
                .display_name(registerInfo.getDisplayName())
                .email(registerInfo.getEmail())
                .phone(registerInfo.getPhone())
                .status(0)
                .build();

        user.setPassword(ServiceUtils.encodePassword(registerInfo.getPassword()));
        user.setCreated(System.currentTimeMillis());
        userMapper.insertUser(user);
        Role role =userMapper.findRoleById(registerInfo.getRoleId());
        UserRole userRole = new UserRole(user.getId(), role.getId());
        userMapper.insertUserRole(userRole);
        log.info("Create user {} success!", user.getUsername());
        return user.getId();
    }

    @Override
    public void update(User user) {
        user.setUpdated(System.currentTimeMillis());
        userMapper.update(user);
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findUserByName(username);
        return user;
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
    public String uploadAvatar(int userId, MultipartFile file) {
        return null;
    }
}
