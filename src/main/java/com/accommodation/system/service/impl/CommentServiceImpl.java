package com.accommodation.system.service.impl;

import com.accommodation.system.dao.PostDao;
import com.accommodation.system.entity.Comment;
import com.accommodation.system.entity.Post;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.info.CommentInfo;
import com.accommodation.system.entity.model.NotificationMessage;
import com.accommodation.system.mapper.CommentMapper;
import com.accommodation.system.mapper.UserMapper;
import com.accommodation.system.service.CommentService;
import com.accommodation.system.uitls.FirebaseUtil;
import com.accommodation.system.uitls.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    PostDao postDao;

    @Override
    public int addComment(Comment comment) throws IOException {
        commentMapper.insertComment(comment);
        pushNotification(comment);
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

    @Async("threadPoolTaskExecutor")
    @Override
    public void pushNotification(Comment comment) throws IOException {
        Post post = postDao.find(comment.getPostId());
        if (Utils.isNotEmpty(post)) {
            int ownerPost = post.getUserId();
            User user = userMapper.findByUserId(comment.getUserId());
            String postUser = "Có người";
            if (Utils.isNotEmpty(user)) {
                postUser = user.getDisplayName();
            }
            String content = comment.getContent();
            if (content.length() > 50) {
                content.substring(0, 50);
            }
            NotificationMessage notificationMessage = NotificationMessage.builder()
                    .to("/topics/Test")
                    .userId(comment.getUserId())
                    .data(NotificationMessage.Data.builder()
                            .postId(comment.getPostId())
                            .build())
                    .notification(NotificationMessage.Notification.builder()
                            .body("Mô tả: " + content + "...")
                            .color("green")
                            .priority("high")
                            .title(postUser + " bình luận về bài đăng của bạn.")
                            .build()).build();
            FirebaseUtil.send(notificationMessage);
        }

    }
}
