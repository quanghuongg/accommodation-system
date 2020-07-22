package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.define.ContextPath;
import com.accommodation.system.entity.NotificationSetting;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.NotificationService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getNotifications() throws ApiServiceException {
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(notificationService.listNotification(getUserId()))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Notification.UPDATE_READ_AT}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateReadAt(@RequestParam("notification_id") int notificationId) {
        notificationService.updateReadAt(notificationId, System.currentTimeMillis());
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Notification.Setting.SETTING + ContextPath.Notification.Setting.GET}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getNotificationSetting() throws ApiServiceException {
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(notificationService.getNotificationSetting(getUserId()))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Notification.Setting.SETTING + ContextPath.Notification.Setting.CREATE}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createNotificationSetting(@RequestBody NotificationSetting notificationSetting) throws ApiServiceException {
        notificationSetting.setUserId(getUserId());
        notificationService.createNotificationSetting(notificationSetting);
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Notification.Setting.SETTING + ContextPath.Notification.Setting.UPDATE}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateNotificationSetting(@RequestBody NotificationSetting notificationSetting) throws ApiServiceException {
        notificationSetting.setUserId(getUserId());
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(notificationService.updateNotificationSetting(notificationSetting))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


}
