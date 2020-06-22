package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.define.ContextPath;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.PostService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import com.accommodation.system.uitls.Utils;
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
@RequestMapping(value = {ContextPath.Post.POST})
@Api(tags = {"Post API"})
public class PostController extends EzContext {
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    @RequestMapping(value = {ContextPath.Post.CREATE}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @RequestMapping(value = {ContextPath.Post.UPLOAD_IMAGES}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @RequestMapping(value = {ContextPath.Post.SEARCH}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> doSearch(@RequestBody SearchInput searchInput) throws IOException {
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(postService.doSearch(searchInput))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Post.VIEW_DETAIL}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> viewDetail(@RequestParam("post_id") String postId) throws IOException {
        Post post = postService.viewDetail(postId);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(post)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Post.UPDATE}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updatePost(@RequestBody PostRequest postRequest) throws ApiServiceException, IOException {
        int userId = getUserId();
        postService.updatePost(userId, postRequest);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Post.DELETE}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deletePost(@RequestParam("post_id") String postId) throws ApiServiceException, IOException {
        int userId = getUserId();
        postService.deletePost(userId, postId);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
