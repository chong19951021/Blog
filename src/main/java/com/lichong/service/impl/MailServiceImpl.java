package com.lichong.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.lichong.entity.Mail;
import com.lichong.service.MailService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Address;
import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void sendSimpleMail(Mail form) {
        String[] toUsers=null;
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("752124913@qq.com");
            List<String> to = form.getTo();
             toUsers = form.getTo().toArray(new String[to.size()]);
            mailMessage.setTo(toUsers);
            mailMessage.setSubject(form.getSubject());
            mailMessage.setText(form.getText());
            mailSender.send(mailMessage);
        } catch (MailSendException e) {
            // 获取无效地址列表
            String[] invalid = getInvalidAddresses(e);
            if (invalid != null) {
                String[] res = filterByArray(toUsers, invalid);
                sendSimpleMail2(form,res);
            }

        }
    }
    private void sendSimpleMail2(Mail form,String[] str){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("752124913@qq.com");
        mailMessage.setTo(str);
        mailMessage.setSubject(form.getSubject());
        mailMessage.setText(form.getText());
        mailSender.send(mailMessage);
    }
    private static String[] getInvalidAddresses(Throwable e) {
        if (e == null) {
            return null;
        }
        if (e instanceof MailSendException) {
            Exception[] exceptions = ((MailSendException) e).getMessageExceptions();
            for (Exception exception : exceptions) {
                if (exception instanceof SendFailedException) {
                    return getStringAddress(((SendFailedException) exception).getInvalidAddresses());
                }
            }
        }
        if (e instanceof SendFailedException) {
            return getStringAddress(((SendFailedException) e).getInvalidAddresses());
        }
        return null;
    }
    private static String[] getStringAddress(Address[] address) {
        List<String> invalid = new ArrayList<>();
        for (Address a : address) {
            String aa = ((InternetAddress) a).getAddress();
            if (!StringUtils.isEmpty(aa)) {
                invalid.add(aa);
            }
        }
        return invalid.stream().distinct().toArray(String[]::new);
    }
    private static String[] filterByArray(String[] source, String[] filter) {
        List<String> result = new ArrayList<>();
        for (String s : source) {
            boolean contains = false;
            for (String f : filter) {
                if (s.contains(f)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                result.add(s);
            }
        }
        return result.stream().toArray(String[]::new);
    }
    @Override
    public void sendHtmlMail(Mail form) {
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
//            messageHelper.setFrom(mailProperties.getUsername());
//            List<String> to = form.getTo();
//            String[] toUsers = form.getTo().toArray(new String[to.size()]);
//            messageHelper.setTo(toUsers);
//            messageHelper.setSubject(form.getSubject());
//            messageHelper.setText(form.getText(), true);
//
//            // 本地附件
//            if (form.getAttachmentPath() != null) {
//                List<String> pathList = form.getAttachmentPath();
//                for (String attachmentPath : pathList) {
//                    File file = new File(attachmentPath);
//                    if (file.exists()) {
//                        String fileName = file.getName();
//                        FileSystemResource fsr = new FileSystemResource(file);
//                        messageHelper.addAttachment(fileName, fsr);
//                    }
//                }
//            }
//
//            mailSender.send(mimeMessage);
//        } catch (Exception e) {
//            log.error("邮件发送失败", e.getMessage());
//            throw new CustomException(ResultCodeEnum.MAIL_SEND_FAILED);
//        }

    }

    @Override
    public void sendTemplateMail(Mail form) {
        try {
            Context context = new Context();
            context.setLocale(Locale.CHINA);
            context.setVariables(form.getContextVar());
            String templateMail = templateEngine.process(form.getTemplateName(), context);
            form.setText(templateMail);
            sendHtmlMail(form);
        } catch (Exception e) {
//            log.error("邮件发送失败", e.getMessage());
//            throw new CustomException(ResultCodeEnum.MAIL_SEND_FAILED);
        }

    }


}
