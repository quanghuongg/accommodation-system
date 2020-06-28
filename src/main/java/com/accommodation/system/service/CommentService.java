package com.accommodation.system.service;

import com.accommodation.system.entity.Comment;
import com.accommodation.system.entity.info.CommentInfo;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    int addComment(Comment comment) throws IOException;

    List<CommentInfo> getListComment(String postId);

    void pushNotification(Comment comment) throws IOException;

}
