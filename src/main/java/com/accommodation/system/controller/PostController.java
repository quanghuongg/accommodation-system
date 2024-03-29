package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.define.ContextPath;
import com.accommodation.system.entity.Comment;
import com.accommodation.system.entity.info.PostFullInfo;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.entity.request.ViewDetailRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.CommentService;
import com.accommodation.system.service.NotificationService;
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
    @Autowired
    CommentService commentService;

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = {ContextPath.Post.CREATE}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createPost(@RequestParam("title") String title,
                                        @RequestParam("price") long price,
                                        @RequestParam("location") String location,
                                        @RequestParam("description") String description,
                                        @RequestParam("room_type_id") int roomTypeId,
                                        @RequestParam("district_id") int districtId,
                                        @RequestParam("ward_id") int wardId,
                                        @RequestParam("area") int area,
                                        @RequestParam("files") MultipartFile[] files) throws ApiServiceException, IOException {
        int userId = getUserId();
        if (userService.isNotHandlePost(userId)) {
            throw new ApiServiceException("user locked");
        }
        if (ServiceUtils.isEmpty(location) || price == 0 || roomTypeId == 0) {
            throw new ApiServiceException(Constant.OBJECT_EMPTY_FIELD);
        }
        if (Utils.isEmpty(files)) {
            throw new ApiServiceException("images not found");
        }
        if (files.length > 5) {
            throw new ApiServiceException("num of images <=5");
        }
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(title);
        postRequest.setArea(area);
        postRequest.setDescription(description);
        postRequest.setDistrictId(districtId);
        postRequest.setWardId(wardId);
        postRequest.setLocation(location);
        postRequest.setPrice(price);
        postRequest.setRoomTypeId(roomTypeId);
        String postId = postService.createPost(userId, postRequest);
        userService.uploadImages(userId, postId, files);
        notificationService.pushNotificationsMatching(postRequest, postId, userId);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(postId)
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

    @RequestMapping(value = {ContextPath.Post.VIEW_DETAIL}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> viewDetail(@RequestBody ViewDetailRequest viewDetailRequest) throws IOException {
        PostFullInfo post = postService.viewDetail(viewDetailRequest.getPostId(), viewDetailRequest.getLocation());
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(post)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Post.VIEW_DETAIL}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> viewDetail2(@RequestParam("post_id") String postId) throws IOException {
        PostFullInfo post = postService.viewDetail(postId, null);
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
        if (userService.isNotHandlePost(userId)) {
            throw new ApiServiceException("user locked");
        }
        postService.updatePost(userId, postRequest);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Post.UPDATE_STATUS}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updatePostStatus(@RequestBody PostRequest postRequest) throws ApiServiceException, IOException {
        int userId = getUserId();
        if (userService.isNotHandlePost(userId)) {
            throw new ApiServiceException("user locked");
        }
        if (Utils.isNotEmpty(postRequest.getStatus())) {
            postService.updatePostStatus(userId, postRequest);
        }
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
        if (userService.isNotHandlePost(userId)) {
            throw new ApiServiceException("user locked");
        }
        postService.deletePost(userId, postId);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Post.Comment.ADD}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addComment(@RequestBody Comment comment) throws ApiServiceException, IOException {
        if (comment.getPostId() == null || comment.getContent() == null) {
            throw new ApiServiceException("object create empty field");
        }
        int userId = getUserId();
        comment.setUserId(userId);
        comment.setCreatedAt(System.currentTimeMillis());
        commentService.addComment(comment);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {ContextPath.Post.Comment.LIST}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listComment(@RequestParam("post_id") String postId) {
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(commentService.getListComment(postId))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
