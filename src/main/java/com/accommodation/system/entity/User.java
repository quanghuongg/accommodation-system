package com.accommodation.system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
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

    private String description;

    private int role_id;

    public User(Integer id, String username, String display_name, String password, String email, String address, String phone, String avatar, long expired, int status, long created, long updated, String description) {
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
        this.description = description;
    }

    private String list_skill;

}
