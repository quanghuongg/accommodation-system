package com.accommodation.system.service;

import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.request.PostRequest;

public interface PostService {

    void createPost(Integer userId, PostRequest postRequest);

    Post findPost(int userId, int postId);

}
