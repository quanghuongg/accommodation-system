package com.accommodation.system.service;

import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.info.PostFullInfo;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;
import com.accommodation.system.exception.ApiServiceException;

import java.io.IOException;

public interface PostService {

    String createPost(Integer userId, PostRequest postRequest);

    Post findPost(String postId) throws IOException;

    SearchResult loadByIds(SearchInput requestInput) throws IOException;

    SearchResult doSearch(SearchInput searchInput) throws IOException;

    PostFullInfo viewDetail(String postId) throws IOException;

    void updatePost(int userId, PostRequest postRequest) throws ApiServiceException, IOException;

    void deletePost(int userId, String postId) throws IOException, ApiServiceException;
}
