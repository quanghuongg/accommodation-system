package com.api.user.entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feedback {
    private int id;

    private int contract_id;

    private String content;

    private int type;

    private long created;

    private String student;

    private String tutor;

}
