package com.lichong.service;

import com.github.pagehelper.PageInfo;
import com.lichong.entity.Type;

import java.util.List;

public interface TypeService {

    int saveType(Type type);
    List<Type> getTypeBySize(int size);


    Type getType(Long id);

    PageInfo getAllTypeByPage(int i);

    List<Type> getAllType();



    List<Type> getAllTypeAndBlog();

    Type getTypeByName(String name);

    int updateType(Type type);

    void deleteType(Long id);




}