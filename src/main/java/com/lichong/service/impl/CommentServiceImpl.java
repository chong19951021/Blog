package com.lichong.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.dao.BlogDao;
import com.lichong.dao.CommentDao;
import com.lichong.entity.Comment;
import com.lichong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private BlogDao blogDao;

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    @Override
    public PageInfo listCommentByBlogId(Long blogId,int pageNum) {
        //查询出父节点
        PageHelper.startPage(pageNum,10);
        List<Comment> comments = commentDao.findByBlogIdParentIdNull(blogId, Long.parseLong("-1"));
        List objects = new ArrayList<>();
        for(Comment comment : comments){
            Long id = comment.getId();
            String parentNickname1 = comment.getNickname(); // 一级评论的名字
            // 根据父类id和博客id查出该博客下所有二级评论。
            List<Comment> childComments = commentDao.findByBlogIdParentIdNotNull(blogId,id);
            // 查询出子评论
            combineChildren(blogId, childComments, parentNickname1);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
            objects.add(comment);
        }
        PageInfo pageInfo = new PageInfo<>(comments);
        pageInfo.setList(objects);
        return pageInfo;
    }

    @Override
    public int commentCount(Long id) {
        return commentDao.commentCount(id);
    }

    private void combineChildren(Long blogId, List<Comment> childComments, String parentNickname1) {
       //判断是否有二级子评论
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(Comment childComment : childComments){
                String parentNickname = childComment.getNickname();
                // 设置每个二级评论的父类评论的名字
                childComment.setParentNickname(parentNickname1);
                // 把二级评论放到数组里
                tempReplys.add(childComment);
                Long childId = childComment.getId();
                // 查询出子二级评论
                recursively(blogId, childId, parentNickname);
            }
        }
    }

    private void recursively(Long blogId, Long childId, String parentNickname1) {
        //根据子二级评论的id找到子三级评论
        List<Comment> replayComments = commentDao.findByBlogIdAndReplayId(blogId,childId);
        if(replayComments.size() > 0){
            for(Comment replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                replayComment.setParentNickname(parentNickname1);
                Long replayId = replayComment.getId();
                tempReplys.add(replayComment);
                recursively(blogId,replayId,parentNickname);
            }
        }
    }

//    新增评论
    @Override
    public int saveComment(Comment comment) {
        Long id = comment.getParentCommentId();
        Long blogId = comment.getBlogId();
        if (id!=-1){
            comment.setParentComment(commentDao.findByParentCommentId(id));
        }else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        int comments = commentDao.saveComment(comment);
//        文章评论计数
        blogDao.getCommentCountById(comment.getBlogId());
        return comments;
    }

    @Override
    public void deleteCommentDeep(Long id) {
        List<Comment> childComments = commentDao.findSec(id);
        combineChildrenD(childComments);

    }

    @Override
    public Comment findEmail(String nickname) {
        return commentDao.findEmail(nickname);
    }

    private void combineChildrenD(List<Comment> childComments) {
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(Comment childComment : childComments){
                Long childId = childComment.getId();
                commentDao.deleteComment(childId);
                recursivelyD(childId);
            }
        }
    }

    private void recursivelyD(Long childId) {
        List<Comment> replayComments = commentDao.findSec(childId);
        if(replayComments.size() > 0){
            for(Comment replayComment : replayComments){
                Long replayId = replayComment.getId();
                commentDao.deleteComment(replayId);
                recursivelyD(replayId);
            }
        }
    }

    //    删除评论
    @Override
    public void deleteComment(Long id) {
        commentDao.deleteComment(id);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentDao.findCommentById(id);
    }
}