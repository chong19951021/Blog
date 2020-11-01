package com.lichong.service;

import com.lichong.entity.Mail;

public interface MailService {
    void sendSimpleMail(Mail form);

    /**
     * 发送html邮件
     * @param form
     */
    void sendHtmlMail(Mail form);

    /**
     * 发送模板邮件
     * @param form
     */
    void sendTemplateMail(Mail form);


}
