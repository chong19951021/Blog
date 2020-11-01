package com.lichong.service;

import com.github.pagehelper.PageInfo;
import com.lichong.entity.Comment;
import com.lichong.entity.Message;

import java.util.List;

public interface MessageService {
    PageInfo listCommentByBlogId(int pageNum);

    int saveComment(Message message);
    void deleteCommentDeep(Long id);
    Message  findEmail(String nickname);

//    查询子评论
//    List<Comment> getChildComment(Long blogId,Long id);

    //删除评论
    void deleteComment(Long id);
    Message findCommentById(Long id);

}
