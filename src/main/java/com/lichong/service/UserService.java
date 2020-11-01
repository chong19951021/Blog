package com.lichong.service;

import com.lichong.entity.User;

public interface UserService {
    User checkUser(String username, String password);

    User findUser(Long id);

}
