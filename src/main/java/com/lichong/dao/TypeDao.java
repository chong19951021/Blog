package com.lichong.dao;

import com.lichong.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TypeDao {
    List getAllTypeByLim();

    int saveType(Type type);

    Type getType(Long id);

    List<Type> getAllType();

    List<Type> getTypeBySize(int size);

    List<Type> getAllTypeAndBlog();

    Type getTypeByName(String name);

    int updateType(Type type);

    void deleteType(Long id);


}