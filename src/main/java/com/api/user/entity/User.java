package com.api.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private Integer id;

    private String username;

    private String display_name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private String address;

    private String phone;

    private String avatar;

    private long expired;

    private int status;

    private long created;

    private long updated;

    private double hourly_wage;

    private String description;

    //Add  more
    private int role_id;

    private Role role;

    private List<Skill> skills;

    public User(Integer id, String username, String display_name, String password, String email, String address, String phone, String avatar, long expired, int status, long created, long updated, double hourly_wage, String description, int role_id, Role role, List<Skill> skills) {
        this.id = id;
        this.username = username;
        this.display_name = display_name;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.avatar = avatar;
        this.expired = expired;
        this.status = status;
        this.created = created;
        this.updated = updated;
        this.hourly_wage = hourly_wage;
        this.description = description;
        this.role_id = role_id;
        this.role = role;
        this.skills = skills;
    }

    private String list_skill;

}
