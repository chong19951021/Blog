package com.lichong.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.dao.BlogDao;
import com.lichong.entity.*;
import com.lichong.queryvo.*;
import com.lichong.queryvo.DetailedBlog;
import com.lichong.service.BlogService;
import com.lichong.service.CommentService;
import com.lichong.service.TypeService;
import com.lichong.service.UserService;
import com.lichong.util.MarkdownUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.error.Mark;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TypeService typeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Transactional
    @Override
    public Blog getBlogById(Long id) {
        Blog blog = blogDao.getBlogById(id);
        if (blog==null){
            throw new NotFoundExpection("该博客不存在");
        }
        Blog blog1 = new Blog();
        BeanUtils.copyProperties(blog,blog1);

//        blogDao.updateViews(id);
//        Blog re= blogDao.getBlogById(id);
//        BlogVo blogVo = new BlogVo();
//        BeanUtils.copyProperties(re,blogVo);
//        redisTemplate.opsForHash().put(id,"blogVo",blogVo);
        return blog1;
    }

    @Override
    public Blog getBlogByIdShow(Long id) {
        Blog blog= blogDao.getBlogById(id);
        if (blog==null){
            throw new NotFoundExpection("该博客不存在");
        }
        Blog blog1 = new Blog();
        BeanUtils.copyProperties(blog,blog1);
        String content = blog1.getContent();
        String s = MarkdownUtils.markdownToHtmlExtensions(content);
        blog1.setContent(s);

//        blogDao.updateViews(id);
//        Blog re= blogDao.getBlogById(id);
//        BlogVo blogVo = new BlogVo();
//        BeanUtils.copyProperties(re,blogVo);
//        redisTemplate.opsForHash().put(id,"blogVo",blogVo);
        return blog1;

    }

    @Override
    public List<BlogQuery> getAllBlog() {
        return blogDao.getAllBlogQuery();
    }

    @Override
    public PageInfo getBlogsByTypeId(int pageNum,Long id) {
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum,5,orderBy);
        List<Blog> blogs = blogDao.getBlogsByTypeId(id);
        ArrayList<Blog> list = new ArrayList<>();
        for (Blog blog: blogs){
            Type type = typeService.getType(blog.getTypeId());
            User user = userService.findUser(blog.getUserId());
            int comments = commentService.commentCount(blog.getId());
            blog.setType(type);
            blog.setUser(user);
            blog.setCommentCount(comments);
            list.add(blog);
        }
        PageInfo pageInfo = new PageInfo (blogs);
        pageInfo.setList(list);
        return pageInfo;
    }


    @Override
    public void saveMidTable(String[] tagIds, Blog blog) {
        blogDao.saveMidTable(tagIds,blog);

    }


    @Override
    @Transactional
    public int saveBlog(Blog blog,String type) {
        if (blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            blog.setCommentCount(0);
            int i = blogDao.saveBlog(blog);
            if ("fabiao".equals(type))
            {
                rabbitTemplate.convertAndSend("exchangeBlog","blog",blog);
            }
           return i;
        }else {
            Boolean aBoolean = blogDao.publishIf(blog.getId());
            if (!aBoolean){
                if ("fabiao".equals(type))
                {
                    rabbitTemplate.convertAndSend("exchangeBlog","blog",blog);
                }

            }

            blog.setUpdateTime(new Date());
            redisTemplate.delete(blog.getId());
            return blogDao.updateBlog(blog);
        }
    }

    @Override
    public PageInfo getBlogByTagId(int pageNum,Long id) {
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum,5,orderBy);
        List<Blog> blogByTagId = blogDao.getBlogByTagId(id);
        ArrayList<Blog> list = new ArrayList<>();
        for (Blog blog: blogByTagId){
            Type type = typeService.getType(blog.getTypeId());
            User user = userService.findUser(blog.getUserId());
            PageInfo comments = commentService.listCommentByBlogId(blog.getId(),10000);
            blog.setType(type);
            blog.setUser(user);
            blog.setCommentCount(comments.getList().size());
            list.add(blog);
        }
        PageInfo pageInfo = new PageInfo (blogByTagId);
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public List<Blog> getBlogByTagId1(Long id) {
        return blogDao.getBlogByTagId1(id);
    }

    @Override
    public int updateBlog(Blog blog) {
        return 0;
    }

    @Override
    public int deleteBlog(Long id) {
        redisTemplate.delete(id);
       return blogDao.deleteBlog(id);

    }

    @Override
    public PageInfo getBlogBySearch(SearchBlog searchBlog,int pageNum) {
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum,5,orderBy);
        List<BlogQuery> blogQueries = blogDao.searchByTitleOrTypeOrRecommend(searchBlog);
        ArrayList<BlogQuery> list = new ArrayList<>();
        for (BlogQuery blog: blogQueries){
            Type type = typeService.getType(blog.getTypeId());
            User user = userService.findUser(blog.getUserId());
            PageInfo comments = commentService.listCommentByBlogId(blog.getId(),10000);
            blog.setType(type);
            blog.setUser(user);
            blog.setCommentCount(comments.getList().size());
            list.add(blog);
        }
        PageInfo pageInfo = new PageInfo (blogQueries);
        pageInfo.setList(list);
        return pageInfo;


    }

    @Override
    public PageInfo<FirstPageBlog> getAllFirstPageBlog(int i ,String query) {
        String orderBy = "update_time desc";
        PageHelper.startPage(i,5,orderBy);
        List<FirstPageBlog> firstPageBlog = blogDao.getSearchBlog(query);

        PageInfo<FirstPageBlog> pageInfo = new PageInfo<FirstPageBlog>(firstPageBlog);
        return pageInfo;
    }

    @Override
    public PageInfo getRecommendedBlog(int i) {
        String orderBy = "rand()";
        PageHelper.startPage(i,5,orderBy);
        List<Blog> blogs = blogDao.getAllRecommendBlog();
        List<Blog> blogs1 = new ArrayList<Blog>();
        for (Blog blog: blogs){
            Type type = typeService.getType(blog.getTypeId());
            User user = userService.findUser(blog.getUserId());
            PageInfo comments = commentService.listCommentByBlogId(blog.getId(),10000);
            blog.setCommentCount(comments.getList().size());
            blog.setType(type);
            blog.setUser(user);
            blogs1.add(blog);
        }
        PageInfo pageInfo = new PageInfo (blogs);
        pageInfo.setList(blogs1);
        return pageInfo;
    }

    @Override
    public List<FirstPageBlog> getSearchBlog(String query) {
        return null;
    }

    @Override
    public DetailedBlog getDetailedBlog(Long id) {
        return null;
    }

    @Override
    public List<FirstPageBlog> getByTypeId(Long typeId) {
        return null;
    }

    @Override
    public Integer getBlogTotal() {
        return null;
    }

    @Override
    public Integer getBlogViewTotal() {
        return null;
    }

    @Override
    public Integer getBlogCommentTotal() {
        return null;
    }

    @Override
    public Integer getBlogMessageTotal() {
        return null;
    }
}
