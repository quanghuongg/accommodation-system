package com.accommodation.system.entity;

import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contract {

    private int id;

    private int student_id;

    private int tutor_id;

    private String description;

    private int number_hour;

    private long date_from;

    private long date_to;

    private long created;

    private long updated;

    private double total;

    private int status;

    private String tutor;

    private String student;

    private  int skill;

    private  String skill_name;

//    0: pending  1: student pay   2 : totur get money

}
