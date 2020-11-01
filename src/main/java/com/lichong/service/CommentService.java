package com.lichong.service;

import com.github.pagehelper.PageInfo;
import com.lichong.entity.Comment;

import java.util.List;

/**
 * @Description: 博客评论业务层接口
 * @Author: ONESTAR
 * @Date: Created in 13:26 2020/4/5
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
public interface CommentService {

    PageInfo listCommentByBlogId(Long blogId, int pageNum);
    int commentCount(Long id);
    int saveComment(Comment comment);
    void deleteCommentDeep(Long id);
     Comment  findEmail(String nickname);

//    查询子评论
//    List<Comment> getChildComment(Long blogId,Long id);

    //删除评论
    void deleteComment(Long id);
    Comment findCommentById(Long id);


}