package com.lichong.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.entity.Blog;
import com.lichong.entity.Comment;
import com.lichong.entity.Message;
import com.lichong.entity.User;
import com.lichong.service.BlogService;
import com.lichong.service.CommentService;
import com.lichong.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.FutureTask;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    //    查询评论列表
    @GetMapping("/message")
    public String comments(@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, Model model) {
        PageHelper.startPage(pageNum,10);
        PageInfo comments = messageService.listCommentByBlogId(pageNum);
        model.addAttribute("pageInfo", comments);
        return "message";
    }

    //    新增评论
    @PostMapping("/message")
    public String post(Message message, HttpSession session, Model model,int pageNum) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            message.setAvatar(user.getAvatar());
            message.setAdminMessage(true);
        } else {
            //设置头像
            message.setAvatar(avatar);
            message.setAdminMessage(false);
        }

//        if (comment.getParentComment().getId() != null) {
//            comment.setParentCommentId(comment.getParentComment().getId());
//        }
        messageService.saveComment(message);
       PageInfo pageInfo = messageService.listCommentByBlogId(pageNum);
        model.addAttribute("pageInfo", pageInfo);

        return "message :: commentList";
    }

    //    删除评论
    @PostMapping("/deletemessage")
    public String delete( @RequestParam Long id, Model model,int pageNum){
        Message commentById = messageService.findCommentById(id);
        if (commentById.getParentMessageId()==-1){
            messageService.deleteComment(id);
            messageService.deleteCommentDeep(id);
        }else
            messageService.deleteComment(id);
        PageInfo pageInfo = messageService.listCommentByBlogId(pageNum);
        model.addAttribute("pageInfo", pageInfo);
        return "message :: commentList";
    }

    @PostMapping("/emailmessage")
    public String findEmail(@RequestParam  String nickname, Model model){
        Message email = messageService.findEmail(nickname);
        model.addAttribute("CommentEmail",email);
        return "message :: commentList";



    }

}
