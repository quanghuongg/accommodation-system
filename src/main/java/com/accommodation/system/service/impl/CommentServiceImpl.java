package com.accommodation.system.service.impl;

import com.accommodation.system.entity.Comment;
import com.accommodation.system.mapper.CommentMapper;
import com.accommodation.system.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: huongnq4
 * Date:  26/06/2020
 * Time: 14 :56
 * To change this template use File | Settings | File and Code Templates.
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Override
    public int addComment(Comment comment) {
        commentMapper.insertComment(comment);
        return comment.getId();
    }

    @Override
    public List<Comment> getListComment(String postId) {
        return commentMapper.listCommentByPostId(postId);
    }
}
