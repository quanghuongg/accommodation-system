package com.accommodation.system.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Skill {

    int id;

    String name;

    String description;

    private long created;

    private long updated;

    private  int status;
}
