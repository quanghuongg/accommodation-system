package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.define.ContextPath;
import com.accommodation.system.entity.*;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.RegisterRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.security.TokenProvider;
import com.accommodation.system.service.*;
import com.accommodation.system.uitls.AESUtil;
import com.accommodation.system.uitls.ServiceUtils;
import com.accommodation.system.uitls.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = {ContextPath.User.USER})
public class UserController extends EzContext {

    private final MailSendingService mailSendingService;
    private final UserService userService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenProvider;

    @Autowired
    PostService postService;

    @Autowired
    public UserController(MailSendingService mailSendingService, UserService userService) {
        this.mailSendingService = mailSendingService;
        this.userService = userService;
    }

    @RequestMapping(value = {ContextPath.User.GET_ALL}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAllUser() {
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(userService.getAll())
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }

    @RequestMapping(value = {ContextPath.User.INFO_GET}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getInfo() throws ApiServiceException {
        String userName = getUsername();
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(userService.findByName(userName))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @RequestMapping(value = {ContextPath.User.CONFIRM}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> confirmRegister(@RequestParam String id) throws ApiServiceException {
        String userId = AESUtil.decrypt(id);
        User user = userService.findByUserId(Integer.parseInt(userId));
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException(Constant.USER_NOT_EXITED);
        }
        user.setStatus(1);
        userService.update(user);
        notificationService.createNotificationSetting(NotificationSetting.builder()
                .createdAt(System.currentTimeMillis())
                .enable(0)
                .userId(user.getId())
                .build());
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.User.REGISTER}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerInfo) throws Exception {
        User existedUser = userService.findByUsername(registerInfo.getUsername());
        if (ServiceUtils.isNotEmpty(existedUser)) {
            throw new ApiServiceException(Constant.USER_CREATE_EXISTING);
        }
        if (registerInfo.getUsername().isEmpty() || registerInfo.getPassword().isEmpty() || registerInfo.getRoleId() == 0 || ServiceUtils.isEmpty(registerInfo.getEmail())
                || ServiceUtils.isEmpty(registerInfo.getEmail())
        ) {
            throw new ApiServiceException(Constant.OBJECT_EMPTY_FIELD);
        }

        if (!ServiceUtils.isValidMail(registerInfo.getEmail())) {
            throw new ApiServiceException("email invalid");
        }
        if (userService.checkEmailExisted(registerInfo.getEmail())) {
            throw new ApiServiceException("email existed");
        }
        if (registerInfo.getPhone() != null) {
            if (!ServiceUtils.isValidPhone(registerInfo.getPhone())) {
                throw new ApiServiceException("phone invalid");
            }
        }
        int userId = userService.save(registerInfo);
        mailSendingService.mailConfirmRegister(registerInfo.getEmail(), registerInfo.getDisplayName(), userId);
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


    @RequestMapping(value = {ContextPath.User.UPDATE_USER_INFO}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateUser(@RequestBody User user) throws ApiServiceException {
        String userName = getUsername();
        User existedUser = userService.findByUsername(userName);
        if (ServiceUtils.isEmpty(existedUser)) {
            throw new ApiServiceException("User not existed");
        }
        userService.update(existedUser, user);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.User.LOGOUT}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> logout() {
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.User.UPLOAD_AVATAR}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> uploadUserAvatar(@RequestParam("avatar") MultipartFile file) throws Exception {
        if (Utils.isEmpty(file) || file.isEmpty()) {
            throw new ApiServiceException("File not found");
        }
        Response responseObject = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(userService.uploadAvatar(getUserId(), file))
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.User.ADD_USER_PIN}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addUserPin(@RequestParam String post_id) throws ApiServiceException, IOException {
        int userId = getUserId();
        Post post = postService.findPost(post_id);
        if (Utils.isEmpty(post)) {
            throw new ApiServiceException("Post not existed");
        }
        Response responseObject = Response.builder()
                .code(0)
                .data(userService.addUserPin(userId, post_id))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }

    @RequestMapping(value = {ContextPath.User.UN_USER_PIN}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> unUserPin(@RequestParam String post_id) throws ApiServiceException, IOException {
        int userId = getUserId();
        userService.unUserPin(userId, post_id);
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);

    }

    @RequestMapping(value = {ContextPath.User.LIST_USER_PIN}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addUserPin() throws IOException, ApiServiceException {
        int userId = getUserId();
        List<UserPin> userPins = userService.listUserPin(userId);
        SearchResult searchResult = null;
        if (Utils.isNotEmpty(userPins) && userPins.size() > 0) {
            SearchInput requestInput = new SearchInput();
            requestInput.setIds(userPins.stream().map(UserPin::getPostId).collect(Collectors.toList()));
            searchResult = postService.loadByIds(requestInput);
        }
        Response responseObject = Response.builder()
                .code(0)
                .data(searchResult)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.User.LIST_POST}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listUserPost(@RequestBody SearchInput requestInput) throws IOException, ApiServiceException {
        int userId = getUserId();
        requestInput.setUserId(userId);
        Response responseObject = Response.builder()
                .code(0)
                .data(postService.doSearch(requestInput))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.User.VIEW_MY_POST}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> viewMyPost(@RequestParam("post_id") String postId) throws IOException {
        Response responseObject = Response.builder()
                .code(0)
                .data(postService.viewMyPost(postId))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @Autowired
    PointService pointService;

    @RequestMapping(value = {ContextPath.User.FEED_BACK}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> feedBack(@RequestBody Feedback feedback) throws Exception {
        int userId = getUserId();
        feedback.setUserFeedBackId(userId);
        Feedback check = pointService.findByPostId(userId, feedback.getPostId());
        if (Utils.isEmpty(check)) {
            feedback.setCreatedAt(System.currentTimeMillis());
            Post post = postService.findPost(feedback.getPostId());
            if (Utils.isNotEmpty(post)) {
                feedback.setUserPostId(post.getUserId());
            }
            pointService.insertFeedback(feedback);
            UserPoint userPoint = pointService.findByUserId(feedback.getUserPostId());
            User userPost = userService.findByUserId(feedback.getUserPostId());
            if (Utils.isNotEmpty(userPoint)) {
                pointService.updatePoint(userPoint.getUserId(), userPoint.getPoint() - 1);
                //if point == 0
                //hidden all post of user post
                if (userPoint.getPoint() - 1 == 0 || true) {
                    postService.hideAllPost(userPoint.getUserId());
                    //send mail
                    mailSendingService.mailFeedback(userPost.getDisplayName(), userPost.getEmail());
                    //push notify
                    notificationService.pushNotificationFeedback(userPost.getDisplayName(),24);
                }

            }
            User userFb = userService.findByUserId(feedback.getUserFeedBackId());
            mailSendingService.mailToAdmin(userFb.getDisplayName(), userPost.getDisplayName(), feedback.getUserPostId() + "", feedback.getPostId(), feedback.getContent());
        }
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


}

