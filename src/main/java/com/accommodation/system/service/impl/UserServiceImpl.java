package com.accommodation.system.service.impl;

import com.accommodation.system.entity.*;
import com.accommodation.system.mapper.ManageMapper;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {


    private UserMapper userMapper;

    private ManageMapper manageMapper;

    public UserServiceImpl(UserMapper userMapper, ManageMapper manageMapper) {
        this.userMapper = userMapper;
        this.manageMapper = manageMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUserByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User [" + username + "] not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), getAuthorities(user));
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userMapper.findRoleByUserId(user.getId()).getName()));
        return authorities;
    }

    @Override
    public List<User> getAll() {
        return userMapper.findUserAll();
    }


    @Override
    public int save(User user) {
        user.setPassword(ServiceUtils.encodePassword(user.getPassword()));
        user.setStatus(0);
        user.setCreated(System.currentTimeMillis());
        userMapper.insertUser(user);
        user.setRole(userMapper.findRoleById(user.getRole_id()));
        UserRole userRole = new UserRole(user.getId(), user.getRole_id());
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
        //Get list Skill
        if (ServiceUtils.isNotEmpty(user))
            user.setSkills(userMapper.listSkillByUser(user.getId()));
        return user;
    }

    @Override
    public User findByUserId(int userId) {
        User user = userMapper.findByUserId(userId);
        if (ServiceUtils.isNotEmpty(user))
            user.setSkills(userMapper.listSkillByUser(user.getId()));
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
    public void addSkill(int userId, List<Integer> skillIds) {
        List<Skill> list = manageMapper.listAllSkill();
        List<Skill> userSkills = userMapper.listSkillByUser(userId);
        if (skillIds.size() > 0) {
            for (int skillId : skillIds) {
                if (list.stream().map(trans -> trans.getId()).collect(Collectors.toList()).contains(skillId) &&
                        !userSkills.stream().map(trans -> trans.getId()).collect(Collectors.toList()).contains(skillId)) {
                    userMapper.insertUserSkill(new UserSkill(userId, skillId));
                }
            }
        }
    }
}
