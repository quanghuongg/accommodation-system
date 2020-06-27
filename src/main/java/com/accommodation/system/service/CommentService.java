package com.accommodation.system.service;

import com.accommodation.system.entity.Comment;
import com.accommodation.system.entity.info.CommentInfo;

import java.util.List;

public interface CommentService {
    int addComment(Comment comment);

    List<CommentInfo> getListComment(String postId);
}
