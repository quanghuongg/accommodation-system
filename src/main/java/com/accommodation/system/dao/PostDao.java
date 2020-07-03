package com.accommodation.system.dao;

import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.model.SearchResult;
import com.accommodation.system.entity.request.PostRequest;
import com.accommodation.system.entity.request.SearchInput;

import java.io.IOException;
import java.util.List;

public interface PostDao {
    Post find(String id) throws IOException;

    Post findByUser(String id, int userId) throws IOException;

    String createPost(Post document);

    SearchResult loadByIds(SearchInput requestInput) throws IOException;

    SearchResult loadBySearchRequest(SearchInput searchInput) throws IOException;

    void deletePost(String postId);

    void updatePost(PostRequest postRequest) throws IOException;

    void updateImage( String postId, List<String> images) throws IOException;

}
