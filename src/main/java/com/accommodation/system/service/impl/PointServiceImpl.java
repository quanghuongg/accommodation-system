package com.accommodation.system.service.impl;

import com.accommodation.system.entity.Feedback;
import com.accommodation.system.entity.UserPoint;
import com.accommodation.system.mapper.FeedbackMapper;
import com.accommodation.system.mapper.UserPointMapper;
import com.accommodation.system.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: huongnq4
 * Date:  24/07/2020
 * Time: 16 :06
 * To change this template use File | Settings | File and Code Templates.
 */
@Service
@Slf4j
public class PointServiceImpl implements PointService {
    @Autowired
    FeedbackMapper feedbackMapper;

    @Autowired
    UserPointMapper userPointMapper;

    @Override
    public void insertFeedback(Feedback feedback) {
        feedbackMapper.insertFeedback(feedback);
    }

    @Override
    public void insertPoint(UserPoint userPoint) {
        userPointMapper.insertPoint(userPoint);
    }

    @Override
    public UserPoint findByUserId(int userId) {
        return userPointMapper.findByUserId(userId);
    }

    @Override
    public void updatePoint(int userId, int point) {
        log.info("update point of userId {}", userId);
        userPointMapper.updatePoint(userId, point);
    }

    @Override
    public Feedback findByPostId(int userId, String postId) {
        return feedbackMapper.findByPostId(userId, postId);
    }
}
