package com.accommodation.system.service;

import com.accommodation.system.entity.Feedback;
import com.accommodation.system.entity.UserPoint;

public interface PointService {
    void insertFeedback(Feedback feedback);

    void insertPoint(UserPoint userPoint);

    UserPoint findByUserId(int userId);

    void updatePoint(int userId, int point);
}
