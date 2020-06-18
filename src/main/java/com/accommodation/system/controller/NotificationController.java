package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.define.ContextPath;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.NotificationService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: huongnq4
 * Date:  18/06/2020
 * Time: 14 :22
 * To change this template use File | Settings | File and Code Templates.
 */
@Slf4j
@RestController
@RequestMapping(value = {ContextPath.Notification.NOTIFICATION})
@Api(tags = {"NotificationController API"})
public class NotificationController extends EzContext {

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = {ContextPath.Notification.GET}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAllUser() throws ApiServiceException {
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(notificationService.getNotifications(getUserId()))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }
}
