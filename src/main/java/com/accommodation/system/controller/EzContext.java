package com.accommodation.system.controller;

import com.accommodation.system.entity.User;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.UserService;
import com.accommodation.system.utils2.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 10 :21
 * To change this template use File | Settings | File and Code Templates.
 */
@Component
public class EzContext {
    @Autowired
    UserService userService;

    public String getUsername() throws ApiServiceException {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        return authUser.getName();
    }

    public int getUserId() throws ApiServiceException {
        String username = getUsername();
        User user = userService.findByUsername(username);
        if (Utils.isEmpty(user)) {
            throw new ApiServiceException("User not found");
        }
        return user.getId();
    }
}
