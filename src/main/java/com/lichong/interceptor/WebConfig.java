package com.lichong.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description: 指定拦截内容的配置类
 * @Author: ONESTAR
 * @Date: Created in 13:57 2020/3/27
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
@Configuration // 申明此类为配置类
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/admin/**") // 任意请求
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/login");
    }
}