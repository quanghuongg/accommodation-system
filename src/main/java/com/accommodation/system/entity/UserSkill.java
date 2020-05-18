package com.accommodation.system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSkill {
    private Integer id;

    private Integer user_id;

    private Integer skill_id;

    public UserSkill(Integer userId, Integer skillId) {
        this.user_id = userId;
        this.skill_id = skillId;
    }
}
