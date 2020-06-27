package com.accommodation.system.service.impl;

import com.accommodation.system.entity.Comment;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.info.CommentInfo;
import com.accommodation.system.mapper.CommentMapper;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.service.CommentService;
import com.accommodation.system.uitls.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    UserMapper userMapper;

    @Override
    public int addComment(Comment comment) {
        commentMapper.insertComment(comment);
        return comment.getId();
    }

    @Override
    public List<CommentInfo> getListComment(String postId) {
        List<Comment> comments = commentMapper.listCommentByPostId(postId);
        List<CommentInfo> commentInfos = new ArrayList<>();
        if (Utils.isNotEmpty(comments) && comments.size() > 0) {
            for (Comment comment : comments) {
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setContent(comment.getContent());
                commentInfo.setCreatedAt(comment.getCreatedAt());
                User user = userMapper.findByUserId(comment.getUserId());
                if (Utils.isNotEmpty(user)) {
                    commentInfo.setUserComment(user.getDisplayName());
                    commentInfo.setAvatarUerComment(user.getAvatar());
                }
                commentInfos.add(commentInfo);
            }
        }
        return commentInfos;
    }
}
