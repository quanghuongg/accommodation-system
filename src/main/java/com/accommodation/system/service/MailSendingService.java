package com.accommodation.system.service;

public interface MailSendingService {
    void mailConfirmRegister(String email, String fullName, int userId) throws Exception;

    void mailResetPassword(String email, String display_name, String newPassword) throws Exception;

    void mailT() throws Exception;
    void mailT2() throws Exception;


}
