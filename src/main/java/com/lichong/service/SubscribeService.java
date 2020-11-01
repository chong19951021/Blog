package com.lichong.service;

import com.lichong.entity.Subscribe;

import java.util.List;

public interface SubscribeService {
    int savaSubscribe(Subscribe subscribe);
    int delSubscribe(String email);
    int delSubscribeById(Long id);
    Subscribe findExist(Subscribe subscribe);
    List<Subscribe> findAllEmails();
}
