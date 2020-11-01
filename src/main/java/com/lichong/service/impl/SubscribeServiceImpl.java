package com.lichong.service.impl;

import com.lichong.dao.SubscribeDao;
import com.lichong.entity.Subscribe;
import com.lichong.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscribeServiceImpl implements SubscribeService {
    @Autowired
    private SubscribeDao subscribeDao;

    @Override
    public int savaSubscribe(Subscribe subscribe) {
        subscribe.setTime(new Date());
        System.out.println(new Date());
      return    subscribeDao.savaSubscribe(subscribe);
    }

    @Override
    public int delSubscribe(String email) {
        return subscribeDao.delSubscribe(email);
    }

    @Override
    public int delSubscribeById(Long id) {
        return subscribeDao.delSubscribeById(id);
    }

    @Override
    public Subscribe findExist(Subscribe subscribe) {
        return subscribeDao.findExist(subscribe);
    }

    @Override
    public List<Subscribe> findAllEmails() {
        return subscribeDao.findAllEmails();
    }
}
