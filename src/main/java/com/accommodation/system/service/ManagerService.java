package com.accommodation.system.service;

import com.accommodation.system.entity.Contract;
import com.accommodation.system.entity.Skill;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.RequestInfo;

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
