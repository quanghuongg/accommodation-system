package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.PostService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(value = {"/post"})
@Api(tags = {"Post API"})
public class PostController extends EzContext {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    @RequestMapping(value = {"/create"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) throws ApiServiceException {
        String userName = getUsername();
        User user = userService.findByUsername(userName);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("User not existed");
        }
        if (ServiceUtils.isEmpty(postRequest.getLocation()) || ServiceUtils.isEmpty(postRequest.getPrice()) ||
                ServiceUtils.isEmpty(postRequest.getRoomTypeId())) {
            throw new ApiServiceException(Constant.OBJECT_EMPTY_FIELD);
        }
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(postService.createPost(user.getId(), postRequest))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/upload-image"}, method = RequestMethod.POST, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<?> uploadImages(@RequestParam("postId") int postId, @RequestParam("files") MultipartFile[] files) throws ApiServiceException {
        String userName = getUsername();
        User user = userService.findByUsername(userName);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("User not existed");
        }
        Post post = postService.findPost(user.getId(), postId);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(post)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
