package com.accommodation.system.service;

public interface MailSendingService {
    void mailConfirmRegister(String email, String fullName, int userId) throws Exception;

    void mailToAdmin(String userFeedBack, String userPost, String userId, String postId, String content) throws Exception;


}
