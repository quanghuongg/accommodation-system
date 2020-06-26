package com.accommodation.system.service;

import com.accommodation.system.entity.Comment;

import java.util.List;

public interface CommentService {
    int addComment(Comment comment);

    List<Comment> getListComment(String postId);
}
