package com.lichong.dao;

import com.lichong.entity.Subscribe;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SubscribeDao {
   int savaSubscribe(Subscribe subscribe);
   int delSubscribe(String email);
   int delSubscribeById(Long id);
   Subscribe findExist(Subscribe subscribe);
   List<Subscribe> findAllEmails();
}
