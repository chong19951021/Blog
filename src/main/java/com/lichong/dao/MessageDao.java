package com.lichong.dao;

import com.lichong.entity.Comment;
import com.lichong.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface MessageDao {
    List<Message> findByBlogIdParentIdNull( @Param("blogParentId") Long blogParentId);

    //查询一级回复
    List<Message> findByBlogIdParentIdNotNull( @Param("id") Long id);

    //查询二级回复
    List<Message> findByBlogIdAndReplayId( @Param("childId") Long childId);

    //查询父级对象
    Message findByParentCommentId(Long parentCommentId);

    //添加一个评论
    int saveComment(Message message);

    //删除评论
    void deleteComment(Long id);
    Message findCommentById(Long id);
    List<Message> findSec(Long parentId);
    Message findEmail(String nickname);
}
