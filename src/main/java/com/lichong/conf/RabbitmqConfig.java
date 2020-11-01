package com.lichong.conf;

import org.springframework.amqp.core.*;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitmqConfig {
    /** 邮件 **/
    @Bean
    public Queue mailQueue() {
        return new Queue("queue1", true, false, false, null);
    }
    @Bean
    public Queue blogQueue() {
        return new Queue("queueBlog", true, false, false, null);
    }

    @Bean
    public Exchange mailExchange() {
        return new DirectExchange("exchange1", true, false, null);
    }
    @Bean
    public Exchange blogExchange() {
        return new DirectExchange("exchangeBlog", true, false, null);
    }

    @Bean
    public Binding orderBinding() {
        return new Binding("queue1", Binding.DestinationType.QUEUE, "exchange1",
                "email", null);
    }
    @Bean
    public Binding blogBinding() {
        return new Binding("queueBlog", Binding.DestinationType.QUEUE, "exchangeBlog",
                "blog", null);
    }
//
//    /** json输出 **/
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}
