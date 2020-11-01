package com.lichong.blog;

import com.lichong.entity.Blog;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class BlogApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void testRedisSet() {
        try {
            redisTemplate.opsForValue().set("test","This is a Springboot-Redis test!");
        } catch (Exception e){
            System.out.println(e.toString());
        }

    }
    @Test
    public void testRedisGet() {
        try {
            String key="test";
            Boolean isHas = redisTemplate.hasKey(key);
            if (isHas){
                Object test = redisTemplate.opsForValue().get(key);
                System.out.println(test);
            }else {
                System.out.println("抱歉！不存在key值为"+key);
            }
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
    @Test
    public void test03(){
        Blog e = new Blog();
        e.setContent("Dasdasdasd");
        e.setTitle("chong");

    }
    @Test
    public void test04(){

        redisTemplate.delete("200");
    }
    @Test
    public void sendSampleMail() {
        // 简单邮件类
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        // 寄件人，默认是配置的username
        mailMessage.setFrom(mailProperties.getUsername());
        // 收件人，支持多个收件人
        mailMessage.setTo("chong_1995@aliyun.com");
        // 邮件主题
        mailMessage.setSubject("Test simple mail");
        // 邮件的文本信息
        mailMessage.setText("Hello this is test mail from java");

        // 发送邮件
        mailSender.send(mailMessage);
    }


}
