package com.accommodation.system.controller;

import com.accommodation.system.service.MailSendingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * User: huongnq4
 * Date:  26/05/2020
 * Time: 18 :01
 * To change this template use File | Settings | File and Code Templates.
 */

//@Component
@Slf4j
public class SendMail {
    @Autowired
    MailSendingService mailSendingService;

    @Scheduled(cron = "*/2 * * * * *")
    public void scheduleTaskUsingCronExpression() throws Exception {

    }

}
