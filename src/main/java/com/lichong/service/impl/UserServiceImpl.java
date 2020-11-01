package com.lichong.service.impl;

import com.lichong.dao.UserDao;
import com.lichong.entity.User;
import com.lichong.service.UserService;
import com.lichong.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User user = userDao.findByUsernameAndPassword(username, password);
        return user;
    }

    @Override
    public User findUser(Long id) {
        return userDao.findUser(id);
    }
}