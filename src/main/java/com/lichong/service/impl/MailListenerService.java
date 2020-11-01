package com.lichong.service.impl;

import com.lichong.entity.Blog;
import com.lichong.entity.Mail;
import com.lichong.entity.Subscribe;
import com.lichong.service.MailService;
import com.lichong.service.SubscribeService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
@Service
public class MailListenerService {
    @Autowired
    private MailService mailService;
    @Autowired
    private SubscribeService subscribeService;

    @RabbitListener(queues = "queue1")
    public void sendRegisterMail(Message message, Channel channel, com.lichong.entity.Message form) throws IOException {
//        log.info("为用户发送注册信息:[{}]", form.getUsername());

        try {
            Mail mailForm = new Mail();
//            mailForm.setFrom("752124913@qq.com");
            mailForm.setSubject("留言通知");
            String content = form.getContent();
            String name = form.getNickname();
            String email1 = form.getEmail();
            String str = "宠，名字为"+name+"的小伙伴对你说："+content+"。他的邮箱地址为："+email1+"。快快联系他吧！";

            mailForm.setText(str);
            String email ="chong_1995@aliyun.com";
            List list =new ArrayList();
            list.add(email);
            mailForm.setTo(list);
//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("username", form.getUsername());
//            userMap.put("password", form.getPassword());
//            mailForm.setTo(Arrays.asList(form.getEmail())).setSubject("注册通知").setTemplateName("register")
//                    .setContextVar(userMap);
            mailService.sendSimpleMail(mailForm);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//            log.info("邮件发送成功");
        } catch (IOException e) {
//            log.error("邮件发送失败", e.getMessage());
            // 回复消息处理失败，并重新入队
            // channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }
    @RabbitListener(queues = "queueBlog")
    public void sendNewBlogMail(Message message, Channel channel, Blog form) throws IOException {
        try {
            Mail mailForm = new Mail();
//            mailForm.setFrom("752124913@qq.com");
            List<Subscribe> allEmails = subscribeService.findAllEmails();
            List list1 = new ArrayList<>();
            for (Subscribe subscribe:allEmails){
                list1.add(subscribe.getEmail());
            }
            mailForm.setSubject("订阅通知");
            String title = form.getTitle();
            String str = "您订阅的博主："+form.getUser().getUsername()+"刚刚发表了题目为：《"+title+"》的文章，赶紧去看看吧！\n博客地址：www.lchub.icu/blog/"+form.getId();
            mailForm.setText(str);
            mailForm.setTo(list1);
//            Map<String, Object> userMap = new HashMap<>();
//            userMap.put("username", form.getUsername());
//            userMap.put("password", form.getPassword());
//            mailForm.setTo(Arrays.asList(form.getEmail())).setSubject("注册通知").setTemplateName("register")
//                    .setContextVar(userMap);
            mailService.sendSimpleMail(mailForm);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
//            log.info("邮件发送成功");
        } catch (IOException e) {
//            log.error("邮件发送失败", e.getMessage());
            // 回复消息处理失败，并重新入队
            // channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }


    }
