package com.accommodation.system.service.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.RoomType;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.info.MyPost;
import com.accommodation.system.entity.info.PostFullInfo;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.mapper.DistrictMapper;
import com.accommodation.system.mapper.RoomTypeMapper;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.mapper.WardMapper;
import com.accommodation.system.service.AmazonS3Service;
import com.accommodation.system.service.CommentService;
import com.accommodation.system.service.PostService;
import com.accommodation.system.uitls.Highlighter;
import com.accommodation.system.uitls.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service(value = "postService")
public class PostServiceImpl implements PostService {
    @Autowired
    AmazonS3Service amazonS3Service;

    @Autowired
    PostDao postDao;

    @Autowired
    DistrictMapper districtMapper;

    @Autowired
    WardMapper wardMapper;

    @Autowired
    RoomTypeMapper roomTypeMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommentService commentService;

    @Override
    public String createPost(Integer userId, PostRequest postRequest) {
        StringBuilder location = new StringBuilder();
        location.append(postRequest.getLocation());
        location.append(", ");
        if (postRequest.getWardId() > 0) {
            location.append(wardMapper.findWard(postRequest.getWardId()).getName());
            location.append(", ");
        }
        if (postRequest.getDistrictId() > 0) {
            location.append(districtMapper.findDistrict(postRequest.getDistrictId()).getName());
            location.append(", ");
        }
        location.append("TP. Hồ Chí Minh");


        Post post = Post.builder()
                .description(postRequest.getDescription())
                .createdAt(System.currentTimeMillis())
                .isVerified(0)
                .location(location.toString())
                .price(postRequest.getPrice())
                .roomTypeId(postRequest.getRoomTypeId())
                .userId(userId)
                .districtId(postRequest.getDistrictId())
                .wardId(postRequest.getWardId())
                .area(postRequest.getArea())
                .title(postRequest.getTitle())
                .status(1)
                .build();
        return postDao.createPost(post);
    }

    @Override
    public Post findPost(String postId) throws IOException {
        return postDao.find(postId);
    }

    @Override
    public SearchResult loadByIds(SearchInput requestInput) throws IOException {
        return postDao.loadByIds(requestInput);
    }


    @Override
    public SearchResult doSearch(SearchInput searchInput) throws IOException {
        return postDao.loadBySearchRequest(searchInput);
    }

    @Override
    public PostFullInfo viewDetail(String postId, String location) throws IOException {
        Post post = postDao.find(postId);
        if (Utils.isEmpty(post)) {
            return null;
        }
        PostFullInfo postFullInfo = new PostFullInfo();
        postFullInfo.setId(postId);
        postFullInfo.setArea(post.getArea());
        postFullInfo.setCreatedAt(post.getCreatedAt());
        postFullInfo.setDescription(post.getDescription());
        RoomType roomType = roomTypeMapper.find(post.getRoomTypeId());
        if (Utils.isNotEmpty(roomType))
            postFullInfo.setRoomType(roomType.getName());
        if (post.getStatus() == null) {
            postFullInfo.setStatus(1);
        } else {
            postFullInfo.setStatus(post.getStatus());
        }
        postFullInfo.setTitle(post.getTitle());
        postFullInfo.setPrice(post.getPrice());
        if (Utils.isNotEmpty(post.getImages())) {
            postFullInfo.setImages(Arrays.asList(post.getImages()));
        }
        postFullInfo.setLocation(post.getLocation());
        User user = userMapper.findByUserId(post.getUserId());
        if (Utils.isNotEmpty(user)) {
            postFullInfo.setUsePost(user.getDisplayName());
            postFullInfo.setPhone(user.getPhone());
            postFullInfo.setAvatarUserPost(user.getAvatar());
        }
        Set<String> highlighters = new LinkedHashSet<>();
        if (Utils.isNotEmpty(location)) {
            highlighters.addAll(Arrays.asList(location.split(",")));
            Highlighter highlighter = new Highlighter(highlighters);
            String highlighterStr = highlighter.getHighlighted(post.getDescription());
            String highlighterTitle = highlighter.getHighlighted(post.getTitle());
            String highlighterLocation = highlighter.getHighlighted(post.getLocation());
            postFullInfo.setDescription(highlighterStr);
            postFullInfo.setTitle(highlighterTitle);
            postFullInfo.setLocation(highlighterLocation);
        }
        postFullInfo.setCommentInfos(commentService.getListComment(postId));
        return postFullInfo;
    }

    @Override
    public void updatePost(int userId, PostRequest postRequest) throws ApiServiceException, IOException {
        String postId = postRequest.getPostId();
        Post post = postDao.findByUser(postId, userId);
        if (Utils.isEmpty(post)) {
            throw new ApiServiceException("post not found");
        }
        postDao.updatePost(postRequest);
    }

    @Override
    public void updatePostStatus(int userId, PostRequest postRequest) throws ApiServiceException, IOException {
        String postId = postRequest.getPostId();
        Post post = postDao.findByUser(postId, userId);
        if (Utils.isEmpty(post)) {
            throw new ApiServiceException("post not found");
        }
        postDao.updateStatus(postRequest);
    }

    @Override
    public void deletePost(int userId, String postId) throws IOException, ApiServiceException {
        Post post = postDao.findByUser(postId, userId);
        if (Utils.isEmpty(post)) {
            throw new ApiServiceException("post not found");
        }
        postDao.deletePost(postId);
    }

    @Override
    public MyPost viewMyPost(String postId) throws IOException {
        Post post = postDao.find(postId);
        if (Utils.isEmpty(post)) {
            return null;
        }
        MyPost myPost = new MyPost();
        myPost.setId(postId);
        myPost.setArea(post.getArea());
        myPost.setCreatedAt(post.getCreatedAt());
        myPost.setDescription(post.getDescription());
        RoomType roomType = roomTypeMapper.find(post.getRoomTypeId());
        if (Utils.isNotEmpty(roomType))
            myPost.setRoomType(roomType.getName());
        myPost.setTitle(post.getTitle());
        myPost.setPrice(post.getPrice());
        myPost.setImages(post.getImages());
        myPost.setLocation(post.getLocation());
        return myPost;
    }

    @Override
    public void hideAllPost(int userId) throws IOException {
        ObjectMapper oMapper = new ObjectMapper();
        SearchInput requestInput = new SearchInput();
        requestInput.setUserId(userId);
        SearchResult searchResult = postDao.loadBySearchRequest(requestInput);
        if (Utils.isEmpty(searchResult)) {
            for (Object post : searchResult.getHits()) {
                PostRequest postRequest = new PostRequest();
                Map map = oMapper.convertValue(post, Map.class);
                postRequest.setPostId(map.get("id").toString());
                postRequest.setStatus(0);
                postDao.updateStatus(postRequest);
            }
        }
    }
}
