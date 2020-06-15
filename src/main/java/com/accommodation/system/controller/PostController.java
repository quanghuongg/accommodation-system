package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.AmazonS3Service;
import com.accommodation.system.service.PostService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import com.accommodation.system.utils2.Utils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        int userId = getUserId();
        if (ServiceUtils.isEmpty(postRequest.getLocation()) || ServiceUtils.isEmpty(postRequest.getPrice()) ||
                ServiceUtils.isEmpty(postRequest.getRoomTypeId())) {
            throw new ApiServiceException(Constant.OBJECT_EMPTY_FIELD);
        }
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(postService.createPost(userId, postRequest))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/upload-image"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> uploadImages(@RequestParam("post_id") String postId, @RequestParam("files") MultipartFile[] files) throws ApiServiceException, IOException {
        if (Utils.isEmpty(files)) {
            throw new ApiServiceException("images not found");
        }
        if (files.length > 5) {
            throw new ApiServiceException("num of images <=5");
        }
        if (Utils.isEmpty(postService.findPost(postId))) {
            throw new ApiServiceException("post not found");
        }
        int userId = getUserId();
        userService.uploadImages(userId, postId, files);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Autowired
    AmazonS3Service amazonS3Service;

    @RequestMapping(value = {"/search"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> doSearch(@RequestBody SearchInput searchInput) throws IOException {
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(postService.doSearch(searchInput))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/detail"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findDetail(@RequestParam("post_id") String postId) throws IOException {
        Post post = postService.findPost(postId);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(post)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
