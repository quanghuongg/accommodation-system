package com.accommodation.system.controller;

import com.accommodation.system.exception.ApiServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * User: huongnq4
 * Date:  08/06/2020
 * Time: 10 :21
 * To change this template use File | Settings | File and Code Templates.
 */
public class EzContext {
    public String getUsername() throws ApiServiceException {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        return authUser.getName();
    }
}
