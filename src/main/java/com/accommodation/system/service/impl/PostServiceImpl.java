package com.accommodation.system.service.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.mapper.DistrictMapper;
import com.accommodation.system.mapper.WardMapper;
import com.accommodation.system.service.AmazonS3Service;
import com.accommodation.system.service.PostService;
import com.accommodation.system.utils2.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
    public Post viewDetail(String postId) throws IOException {
        Post post = postDao.find(postId);
        if (Utils.isEmpty(post)) {
            return null;
        }
        post.setImages(amazonS3Service.listFileImages(postId));
        return post;
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
    public void deletePost(int userId, String postId) throws IOException, ApiServiceException {
        Post post = postDao.findByUser(postId, userId);
        if (Utils.isEmpty(post)) {
            throw new ApiServiceException("post not found");
        }
        postDao.deletePost(postId);
    }
}
