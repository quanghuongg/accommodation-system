package com.accommodation.system.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 14 :41
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class Notifications {
    private String id;

    private Integer userId;

    private String info;

    private String message;

    private long readAt;

    private long createdAt;

    private long updatedAt;

    private String type;

}
