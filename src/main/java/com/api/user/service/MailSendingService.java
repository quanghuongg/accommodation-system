package com.api.user.service;

public interface MailSendingService {
    void mailConfirmRegister(String email, String fullName, int userId) throws Exception;

    void mailResetPassword(String email, String display_name, String newPassword) throws Exception;

}
