package com.accommodation.system.entity.model;

import com.accommodation.system.entity.User;
import com.accommodation.system.entity.base.BaseModel;
import lombok.*;

import java.util.List;

/**
 * User: huongnq4
 * Date:  22/06/2020
 * Time: 15 :17
 * To change this template use File | Settings | File and Code Templates.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ListUser extends BaseModel {
    private List<User> users;

    private int total;

    private int page;

    private int size;
}
