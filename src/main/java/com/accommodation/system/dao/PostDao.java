package com.accommodation.system.dao;

import com.accommodation.system.entity.Post;

import java.io.IOException;

public interface PostDao {
    Post find(String id) throws IOException;

    String createPost(Post document);
}
