package com.accommodation.system.service.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Role;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.UserPin;
import com.accommodation.system.entity.UserRole;
import com.accommodation.system.entity.info.UserFullInfo;
import com.accommodation.system.entity.request.RegisterRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.mapper.UserPinMapper;
import com.accommodation.system.service.AmazonS3Service;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    AmazonS3Service amazonS3Service;

    @Autowired
    UserPinMapper userPinMapper;

    private UserMapper userMapper;

    @Autowired
    PostDao postDao;

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
                .status(1)
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
    public int save(User user) {
        user.setStatus(1);
        user.setCreatedAt(System.currentTimeMillis());
        userMapper.insertUser(user);
        Role role = userMapper.findRoleById(1);
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
    public String uploadAvatar(int userId, MultipartFile file) throws Exception {
        User currentUser = userMapper.findByUserId(userId);
        try {
            String strPath = Constant.FileUploader.PATH_AVATARS;
            String fileName = userId + Constant.FileUploader.MediaType.IMAGE_EXTENSION;
            File fileUpload = new File(fileName);
            ImageIO.write(handleImage(file.getInputStream()), "jpg", fileUpload);
            amazonS3Service.uploadFile(strPath + "/" + fileName, fileUpload);
            String avatarPathToSave = Constant.HOST_STATIC + "avatars/" + fileName;
            currentUser.setAvatar(avatarPathToSave);
            userMapper.update(currentUser);
            return avatarPathToSave;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApiServiceException("[Service] [upload avatar] cannot save avatar file (exception)");
        }
    }

    @Override
    public int addUserPin(int userId, String postId) {
        UserPin userPin = UserPin.builder()
                .userId(userId)
                .postId(postId)
                .createdAt(System.currentTimeMillis())
                .enable(1).build();
        userPinMapper.insertUserPin(userPin);
        return userPin.getId();
    }

    @Override
    public void unUserPin(int userId, String post_id) {
        userPinMapper.unUserPin(userId, post_id);
    }

    @Override
    public List<UserPin> listUserPin(int userId) {
        return userPinMapper.listUserPin(userId);
    }


    private BufferedImage handleImage(InputStream image) throws Exception {
        BufferedImage bufferedImage = null;
        BufferedImage requestBufferedImage = null;
        try {
            requestBufferedImage = ImageIO.read(image);
        } catch (Exception e) {
            throw new ApiServiceException("An error occurred while converting image to JPEG format");
        }

        BufferedImage resizeImage = requestBufferedImage;
        if (requestBufferedImage.getWidth() > 2048) {
            log.info("Image with: {}, bigger than 2048px. Resize image", requestBufferedImage.getWidth());
            resizeImage = Scalr.resize(requestBufferedImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, 2048);
        }

        bufferedImage = new BufferedImage(resizeImage.getWidth(), resizeImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().drawImage(resizeImage, 0, 0, Color.WHITE, null);

        return bufferedImage;
    }

    @Override
    public void uploadImages(int userId, String postId, MultipartFile[] files) throws ApiServiceException {
        int first = 0;
        String host = "https://huongnq.s3-ap-southeast-1.amazonaws.com/";
        try {
            List<String> images = new ArrayList<>();
            String strPath = Constant.FileUploader.PATH_IMAGES + "/" + postId.replaceAll("-", "");
            for (MultipartFile file : files) {
                first = (int) (Math.random() * 1000000);
                String fileName = first + Constant.FileUploader.MediaType.IMAGE_EXTENSION;
                File fileUpload = new File(fileName);
                ImageIO.write(handleImage(file.getInputStream()), "jpg", fileUpload);
                amazonS3Service.uploadFile(strPath + "/" + fileName, fileUpload);
                images.add(host + strPath + "/" + fileName);
            }
            postDao.updateImage(postId, images);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
