package com.accommodation.system.service.impl;

import com.accommodation.system.entity.Contract;
import com.accommodation.system.entity.Role;
import com.accommodation.system.entity.Skill;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.RequestInfo;
import com.accommodation.system.mapper.ContractMapper;
import com.accommodation.system.mapper.ManageMapper;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.service.ManagerService;
import com.accommodation.system.uitls.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service(value = "managerService")
public class ManagerServiceImpl implements ManagerService {

    private UserMapper userMapper;
    private ManageMapper manageMapper;
    private ContractMapper contractMapper;

    @Autowired
    public ManagerServiceImpl(UserMapper userMapper, ManageMapper manageMapper, ContractMapper contractMapper) {
        this.userMapper = userMapper;
        this.manageMapper = manageMapper;
        this.contractMapper = contractMapper;
    }

    @Override
    public List<User> getAllUser(int roleId) {
        List<User> users = new ArrayList<>();
        List<User> list = userMapper.findUserAll();
        for (User user : list) {
            Role role = userMapper.findRoleByUserId(user.getId());
            user.setSkills(userMapper.listSkillByUser(user.getId()));
            if (roleId == 0 && role.getId() != 3) {
                users.add(user);
            } else if (role.getId() == roleId) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public List<User> listTutor(RequestInfo requestInfo) {
        List<User> users = new ArrayList<>();
        List<User> list = null;
        if (ServiceUtils.isNotEmpty(requestInfo.getAddress()) && ServiceUtils.isNotEmpty(requestInfo.getName())) {
            list = userMapper.findUserNameAndAddress(requestInfo.getName(), requestInfo.getAddress());
        } else if (ServiceUtils.isEmpty(requestInfo.getAddress()) && ServiceUtils.isNotEmpty(requestInfo.getName())) {
            list = userMapper.findListUserByName(requestInfo.getName());
        } else if (ServiceUtils.isNotEmpty(requestInfo.getAddress()) && ServiceUtils.isEmpty(requestInfo.getName())) {
            list = userMapper.findUserAddress(requestInfo.getAddress());
        } else {
            list = userMapper.findTutorAll();
        }
        for (User user : list) {
            user.setSkills(userMapper.listSkillByUser(user.getId()));
            users.add(user);
        }
        return list;
    }

    @Override
    public List<User> listTutorBySkill(int skillId) {
        return userMapper.findUserBySkillId(skillId);
    }

    @Override
    public int addSkill(Skill skill) {
        manageMapper.insertSkill(skill);
        return skill.getId();
    }

    @Override
    public List<Skill> listSkill() {
        return manageMapper.listAllSkill();
    }

    @Override
    public Skill findSkillById(int id) {
        return manageMapper.findSkillById(id);
    }


    @Override
    public void updateSkill(Skill skill) {
        skill.setUpdated(System.currentTimeMillis());
        manageMapper.updateSkill(skill);
    }

    @Override
    public List<Contract> getListContract(RequestInfo requestInfo) {
        if (requestInfo.getDateFrom() == 0 || requestInfo.getDateTo() == 0) {
            return contractMapper.getListContract();
        }
        return contractMapper.getListContractByFilter(requestInfo.getDateFrom(), requestInfo.getDateTo(), requestInfo.getStatusContract());
    }
}