package com.lichong.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.dao.BlogDao;
import com.lichong.dao.CommentDao;
import com.lichong.dao.MessageDao;
import com.lichong.entity.Comment;
import com.lichong.entity.Message;
import com.lichong.queryvo.PageVo;
import com.lichong.service.MessageService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Autowired
    private BlogDao blogDao;

    //存放迭代找出的所有子代的集合
    private List<Message> tempReplys = new ArrayList<>();

    @Override
    public PageInfo listCommentByBlogId(int pageNum) {
        //查询出父节点
        PageHelper.startPage(pageNum,10);
        List<Message> comments = messageDao.findByBlogIdParentIdNull( Long.parseLong("-1"));
        List objects = new ArrayList<>();
        for(Message comment : comments){
            Long id = comment.getId();
            String parentNickname1 = comment.getNickname(); // 一级评论的名字
            // 根据父类id和博客id查出该博客下所有二级评论。
            List<Message> childComments = messageDao.findByBlogIdParentIdNotNull(id);
            // 查询出子评论
            combineChildren( childComments, parentNickname1);
            comment.setReplyComments(tempReplys);

            tempReplys = new ArrayList<>();
            objects.add(comment);
        }
        PageInfo pageInfo = new PageInfo<>(comments);
        pageInfo.setList(objects);
        return pageInfo;
    }

    private void combineChildren( List<Message> childComments, String parentNickname1) {
        //判断是否有二级子评论
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(Message childComment : childComments){
                String parentNickname = childComment.getNickname();
                // 设置每个二级评论的父类评论的名字
                childComment.setParentNickname(parentNickname1);
                // 把二级评论放到数组里
                tempReplys.add(childComment);
                Long childId = childComment.getId();
                // 查询出子二级评论
                recursively(childId, parentNickname);
            }
        }
    }

    private void recursively( Long childId, String parentNickname1) {
        //根据子二级评论的id找到子三级评论
        List<Message> replayComments = messageDao.findByBlogIdAndReplayId(childId);
        if(replayComments.size() > 0){
            for(Message replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                replayComment.setParentNickname(parentNickname1);
                Long replayId = replayComment.getId();
                tempReplys.add(replayComment);
                recursively(replayId,parentNickname);
            }
        }
    }

    //    新增评论
    @Override
    public int saveComment(Message message) {
        Long id = message.getParentMessageId();
        if (id!=-1){
            message.setParentMessage(messageDao.findByParentCommentId(id));
        }else {
            message.setParentMessage(null);
        }
        message.setCreateTime(new Date());
        int comments = messageDao.saveComment(message);
        rabbitTemplate.convertAndSend("exchange1","email",message);
//        文章评论计数
        return comments;
    }

    @Override
    public void deleteCommentDeep(Long id) {
        List<Message> childComments = messageDao.findSec(id);
        combineChildrenD(childComments);

    }

    @Override
    public Message findEmail(String nickname) {
        return null;
    }

    private void combineChildrenD(List<Message> childComments) {
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(Message childComment : childComments){
                Long childId = childComment.getId();
                messageDao.deleteComment(childId);
                recursivelyD(childId);
            }
        }
    }

    private void recursivelyD(Long childId) {
        List<Message> replayComments = messageDao.findSec(childId);
        if(replayComments.size() > 0){
            for(Message replayComment : replayComments){
                Long replayId = replayComment.getId();
                messageDao.deleteComment(replayId);
                recursivelyD(replayId);
            }
        }
    }

    //    删除评论
    @Override
    public void deleteComment(Long id) {
        messageDao.deleteComment(id);
    }

    @Override
    public Message findCommentById(Long id) {
        return messageDao.findCommentById(id);
    }
}
