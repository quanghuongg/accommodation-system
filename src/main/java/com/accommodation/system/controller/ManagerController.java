package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.RequestInfo;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.request.RegisterRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = {"/manager"})
@Api(tags = {"ManagerController API"})
public class ManagerController {


    private final UserService userService;


    public ManagerController( UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/add-admin"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addAdmin(@RequestBody RegisterRequest registerRequest) throws ApiServiceException {
        if (registerRequest.getUsername().isEmpty() || registerRequest.getPassword().isEmpty()) {
            throw new ApiServiceException(Constant.OBJECT_EMPTY_FIELD);
        }
        User existedUser = userService.findByUsername(registerRequest.getUsername());
        if (ServiceUtils.isNotEmpty(existedUser)) {
            throw new ApiServiceException(Constant.USER_CREATE_EXISTING);
        }
        if (registerRequest.getDisplayName() == null) {
            registerRequest.setDisplayName(registerRequest.getUsername());
        }
        registerRequest.setRoleId(3);
        userService.save(registerRequest);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/list-user"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAllUser(@RequestBody RequestInfo requestInfo) {
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {"/view-detail"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> viewInfoDetailUser(@RequestParam("user_id") int userId) throws ApiServiceException {
        if (ServiceUtils.isEmpty(userId)) {
            throw new ApiServiceException("empty field");
        }
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/block"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> blockUser(@RequestParam("user_id") int userId) throws ApiServiceException {
        if (userId <= 0) {
            throw new ApiServiceException("userId not found");
        }
        User user = userService.findByUserId(userId);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("userId not found");
        }
        user.setStatus(0);
        userService.update(user);
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/un-block"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> unBlockUser(@RequestParam("user_id") int userId) throws ApiServiceException {
        if (userId <= 0) {
            throw new ApiServiceException("userId not found");
        }
        User user = userService.findByUserId(userId);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("userId not found");
        }
        user.setStatus(1);
        userService.update(user);
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/list-feedback"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listFeedBack(@RequestBody RequestInfo requestInfo) {
        if (requestInfo.getPage() == 0) {
            requestInfo.setPage(1);
        }
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }
}
