package com.accommodation.system.service.impl;

import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.mapper.PostMapper;
import com.accommodation.system.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "postService")
public class PostServiceImpl implements PostService {
    @Autowired
    PostMapper postMapper;

    @Override
    public void createPost(Integer userId, PostRequest postRequest) {
        Post post = Post.builder()
                .description(postRequest.getDescription())
                .createdAt(System.currentTimeMillis())
                .isVerified(0)
                .location(postRequest.getLocation())
                .price(postRequest.getPrice())
                .roomTypeId(postRequest.getRoomTypeId())
                .userId(userId)
                .build();
        postMapper.insertPost(post);
    }

    @Override
    public Post findPost(int userId, int postId) {
        return postMapper.findPost(userId, postId);
    }
}
