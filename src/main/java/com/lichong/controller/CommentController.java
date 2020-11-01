package com.lichong.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.lichong.entity.Blog;
import com.lichong.entity.Comment;
import com.lichong.entity.User;
import com.lichong.queryvo.DetailedBlog;
import com.lichong.service.BlogService;
import com.lichong.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${comment.avatar}")
    private String avatar;

//    查询评论列表
    @GetMapping("/comments")
    public String comments(@RequestParam int pageNum,@RequestParam Long blogId, Model model) {
        PageInfo pageInfo = commentService.listCommentByBlogId(blogId,pageNum);
        Blog blogById = blogService.getBlogById(blogId);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("blog",blogById);
        return "blog :: commentList";
    }

//    新增评论
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session, @RequestParam int pageNum, Model model) {
        String nickname = comment.getNickname();
        String content = comment.getContent();
        String email = comment.getEmail();
        if (nickname==null || nickname.equals("") ||content==null||content.equals("")
        || email==null||email.equals("")){
            Long blogId = comment.getBlogId();
            PageInfo pageInfo = commentService.listCommentByBlogId(blogId,pageNum);
            Blog blogById = blogService.getBlogById(blogId);
            model.addAttribute("pageInfo", pageInfo);
            model.addAttribute("blog",blogById);
            model.addAttribute("message","信息要填完整哦");

            return "blog :: commentList";
        }

        Long blogId = comment.getBlogId();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            //设置头像
            comment.setAvatar(avatar);
        }

//        if (comment.getParentComment().getId() != null) {
//            comment.setParentCommentId(comment.getParentComment().getId());
//        }
        commentService.saveComment(comment);
        PageInfo pageInfo = commentService.listCommentByBlogId(blogId,pageNum);
        List list=  pageInfo.getList();
        String l = JSONObject.toJSONString(list);
        redisTemplate.opsForHash().put(blogId,"comments",l);
        Blog blogById = blogService.getBlogById(blogId);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("blog",blogById);
        return "blog :: commentList";

    }
    @PostMapping("/zhankai")
    public String zhankai(@RequestParam int pageNum, @RequestParam Long blogId,Model model){
        PageInfo pageInfo = commentService.listCommentByBlogId(blogId,pageNum);
        Blog blogById = blogService.getBlogById(blogId);
        model.addAttribute("blog",blogById);
        model.addAttribute("pageInfo",pageInfo);
        return "blog :: commentList";
    }
//    删除评论
    @PostMapping("/delete")
    public String delete(@RequestParam Long blogId, @RequestParam Long id,@RequestParam int pageNum, Model model){
        Comment commentById = commentService.findCommentById(id);
         if (commentById.getParentCommentId()==-1){
             commentService.deleteComment(id);
             commentService.deleteCommentDeep(id);
         }else
        commentService.deleteComment(id);
        PageInfo pageInfo = commentService.listCommentByBlogId(blogId,pageNum);
        Blog blogById = blogService.getBlogById(blogId);
        List list=  pageInfo.getList();
        String l = JSONObject.toJSONString(list);
        redisTemplate.opsForHash().put(blogId,"comments",l);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("blog",blogById);
        return "blog :: commentList";
    }

    @PostMapping("/email")
    public String findEmail(@RequestParam  String nickname, Model model){
        Comment email = commentService.findEmail(nickname);
        model.addAttribute("CommentEmail",email);
        return "blog :: commentList";



    }
}