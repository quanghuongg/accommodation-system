package com.api.user.service;

import com.api.user.entity.Contract;
import com.api.user.entity.Skill;
import com.api.user.entity.User;
import com.api.user.entity.model.RequestInfo;

import java.util.List;

public interface ManagerService {

    List<User> getAllUser(int type);

    int addSkill(Skill skill);

    List<Skill> listSkill();

    Skill findSkillById(int id);

    List<User> listTutor(RequestInfo requestInfo);

    List<User> listTutorBySkill(int skillId);

    void updateSkill(Skill skill);

    List<Contract> getListContract(RequestInfo requestInfo);

}
