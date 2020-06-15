package com.accommodation.system.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * User: huongnq4
 * Date:  15/06/2020
 * Time: 10 :29
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@Builder
public class District implements Serializable {
    private int id;
    private String name;
}
