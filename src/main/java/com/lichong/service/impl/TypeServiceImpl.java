package com.lichong.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.dao.BlogDao;
import com.lichong.dao.TypeDao;
import com.lichong.entity.Blog;
import com.lichong.entity.Type;
import com.lichong.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeDao typeDao;
    @Autowired
    private BlogDao blogDao;

    @Transactional
    @Override
    public int saveType(Type type) {
        return typeDao.saveType(type);
    }

    @Override
    public List<Type> getTypeBySize(int size) {
        return typeDao.getTypeBySize(size);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeDao.getType(id);
    }

    @Transactional
    @Override
    public PageInfo getAllTypeByPage(int i) {
        String orderBy = "id asc";
        PageHelper.startPage(i,5,orderBy);
        List<Type> allType = typeDao.getAllType();
        List<Type> blogs1 = new ArrayList<>();
        for (Type type :allType){
            List<Blog> blogs = blogDao.getBlogsByTypeId(type.getId());
            type.setBlogs(blogs);
            blogs1.add(type);
        }
        PageInfo pageInfo = new PageInfo (allType);
        pageInfo.setList(blogs1);

        return pageInfo;
    }

    @Override
    public List<Type> getAllType() {
        List<Type> allType = typeDao.getAllType();
        for (Type type :allType){
            List<Blog> blogs = blogDao.getBlogsByTypeId(type.getId());
            type.setBlogs(blogs);

        }
        return allType;
    }


    @Override
    public List<Type> getAllTypeAndBlog() {
        return typeDao.getAllTypeAndBlog();
    }

    @Override
    public Type getTypeByName(String name) {
        return typeDao.getTypeByName(name);
    }

    @Transactional
    @Override
    public int updateType(Type type) {
        return typeDao.updateType(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeDao.deleteType(id);
    }



}