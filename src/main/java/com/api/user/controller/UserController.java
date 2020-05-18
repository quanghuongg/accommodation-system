package com.api.user.controller;

import com.api.user.define.Constant;
import com.api.user.entity.Role;
import com.api.user.entity.Skill;
import com.api.user.entity.User;
import com.api.user.entity.model.RequestInfo;
import com.api.user.entity.model.Response;
import com.api.user.exception.ApiServiceException;
import com.api.user.security.TokenProvider;
import com.api.user.service.ContractService;
import com.api.user.service.MailSendingService;
import com.api.user.service.ManagerService;
import com.api.user.service.UserService;
import com.api.user.uitls.AESUtil;
import com.api.user.uitls.ServiceUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = {"/user"})
@Api(tags = {"UserController API"})
public class UserController {

    private final MailSendingService mailSendingService;
    private final UserService userService;
    private final ManagerService managerService;
    private final ContractService contractService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenProvider;

    @Autowired

    public UserController(MailSendingService mailSendingService, UserService userService, ContractService contractService
            , ManagerService managerService) {
        this.mailSendingService = mailSendingService;
        this.userService = userService;
        this.managerService = managerService;
        this.contractService = contractService;
    }

    @RequestMapping(value = {"/get-all"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAllUser() {
        Response responseObject = Response.builder()
                .code(0)
                .data(userService.getAll())
                .message("success")
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }

    @RequestMapping(value = {"/info"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getInfo() throws ApiServiceException, HttpMessageNotReadableException {
        Response response;
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        User user = userService.findByUsername(authUser.getName());
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("User not existed");
        }
        Role role = userService.findRoleByUserId(user.getId());
        user.setRole(userService.findRoleByUserId(user.getId()));
        user.setRole_id(role.getId());

        response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @RequestMapping(value = {"/confirm-register"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> confirmRegister(@RequestParam String id) throws ApiServiceException {
        String userId = AESUtil.decrypt(id);
        User user = userService.findByUserId(Integer.parseInt(userId));
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException(Constant.USER_NOT_EXITED);
        }
        user.setStatus(1);
        userService.update(user);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/reset-password"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> resetPassword(@RequestParam String email) throws Exception {
        User user = userService.findByEmail(email);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException(Constant.USER_NOT_EXITED);
        }
        String newPassword = ServiceUtils.generateRandomString();
        log.info("New password: {}", newPassword);
        user.setPassword(ServiceUtils.encodePassword(newPassword));
        userService.update(user);
        mailSendingService.mailResetPassword(user.getEmail(), user.getDisplay_name(), newPassword);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/register"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody User user) throws Exception {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getRole_id() == 0 || ServiceUtils.isEmpty(user.getEmail())
                || ServiceUtils.isEmpty(user.getEmail())
        ) {
            throw new ApiServiceException(Constant.OBJECT_EMPTY_FIELD);
        }

        if (!ServiceUtils.isValidMail(user.getEmail())) {
            throw new ApiServiceException("email invalid");
        }
        if (userService.checkEmailExisted(user.getEmail())) {
            throw new ApiServiceException("email existed");
        }
        if (user.getPhone() != null) {
            if (!ServiceUtils.isValidPhone(user.getPhone())) {
                throw new ApiServiceException("phone invalid");
            }
        }

        User existedUser = userService.findByUsername(user.getUsername());
        if (ServiceUtils.isNotEmpty(existedUser)) {
            throw new ApiServiceException(Constant.USER_CREATE_EXISTING);
        }
        userService.save(user);
        mailSendingService.mailConfirmRegister(user.getEmail(), user.getDisplay_name(), user.getId());
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = {"/get-token"})
    public ResponseEntity<?> getToken(@RequestBody User user) throws HttpMessageNotReadableException {
        Authentication socialAuthentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), Collections.emptyList()));
        String token = jwtTokenProvider.generateToken(socialAuthentication);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(token)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = {"/list-skill"})
    public ResponseEntity<?> getListKill() throws HttpMessageNotReadableException {
        List<Skill> list = managerService.listSkill();
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(list)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/add-skill"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addSkill(@RequestBody RequestInfo requestInfo) throws ApiServiceException {
        if (requestInfo.getSkillIds().size() == 0) {
            throw new ApiServiceException("skillIds null ");
        }
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        User user = userService.findByUsername(authUser.getName());
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("User not existed");
        }
        userService.addSkill(user.getId(), requestInfo.getSkillIds());
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateUser(@RequestBody User user) throws ApiServiceException {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        User existedUser = userService.findByUsername(authUser.getName());
        if (ServiceUtils.isEmpty(existedUser)) {
            throw new ApiServiceException("User not existed");
        }
        if (ServiceUtils.isNotEmpty(user.getDisplay_name())) {
            existedUser.setDisplay_name(user.getDisplay_name());
        }
        if (ServiceUtils.isNotEmpty(user.getEmail())) {
            if (!ServiceUtils.isValidMail(user.getEmail())) {
                throw new ApiServiceException("email invalid");
            }
            existedUser.setEmail(user.getEmail());
        }
        if (ServiceUtils.isNotEmpty(user.getPassword())) {
            existedUser.setPassword(ServiceUtils.encodePassword(user.getPassword()));
        }
        if (ServiceUtils.isNotEmpty(user.getPhone())) {
            if (!ServiceUtils.isValidPhone(user.getPhone())) {
                throw new ApiServiceException("phone invalid");
            }
            existedUser.setPhone(user.getPhone());
        }
        if (ServiceUtils.isNotEmpty(user.getAddress())) {
            existedUser.setAddress(user.getAddress());
        }
        if (ServiceUtils.isNotEmpty(user.getAvatar())) {
            existedUser.setAvatar(user.getAvatar());
        }
        if (ServiceUtils.isNotEmpty(user.getHourly_wage()) && user.getHourly_wage() != 0) {
            existedUser.setHourly_wage(user.getHourly_wage());
        }
        if (ServiceUtils.isNotEmpty(user.getStatus()) && user.getStatus() != 0) {
            existedUser.setStatus(user.getStatus());
        }
        if (ServiceUtils.isNotEmpty(user.getDescription())) {
            existedUser.setDescription(user.getDescription());
        }
        userService.update(existedUser);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> logout()  {
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

