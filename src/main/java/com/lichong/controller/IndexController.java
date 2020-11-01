package com.lichong.controller;

import com.alibaba.fastjson.*;
import com.github.pagehelper.PageInfo;
import com.lichong.dao.BlogDao;
import com.lichong.entity.*;
import com.lichong.queryvo.*;
import com.lichong.service.*;
import com.lichong.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private TypeService typeService;
    @Autowired
    private UserService userService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TagService tagService;
    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        Map pageinfo1=null;
        PageVo pageVo=null;
        PageInfo pageInfo=null;
        pageinfo1 = redisTemplate.opsForHash().entries("pageinfo1");
         if (pageinfo1.size()==0){
             pageinfo1=null;
         }
         if (pageinfo1==null){
            pageInfo = blogService.getRecommendedBlog(pageNum);
            List list = pageInfo.getList();
            pageVo = new PageVo(list);
            String s = JSONObject.toJSONString(pageVo);
            JSONObject jsonObject = JSONObject.parseObject(s);
            Map<String, String> params = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
            });
             redisTemplate.opsForHash().putAll("pageinfo1",params);
             redisTemplate.expire("pageinfo1",5 , TimeUnit.HOURS);
         }else {
             String list = (String)pageinfo1.get("list");
//             String replace = list.replace("\\", "");
             List<Blog> blogs = JSON.parseArray(list, Blog.class);
             pageInfo =new PageInfo(blogs);
         }
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("pageInfo",pageInfo);
        return "index";
    }
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable long id, HttpServletRequest req,Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
       blogDao.updateViews(id);
        Map entries = redisTemplate.opsForHash().entries(id);
        Blog blog=null;
        PageInfo pageInfo=null;
        if (entries.size()==0){
            entries=null;
        }
        if (entries==null){
            Box box = new Box();
            blog= blogService.getBlogById(id);
            BlogVo blogVo = new BlogVo();
            User user = userService.findUser(blog.getUserId());
            blog.setUser(user);
            BeanUtils.copyProperties(blog,blogVo);
            String tagIds = blog.getTagIds();
            List<Tag> tags = tagService.listTag(tagIds);
            pageInfo = commentService.listCommentByBlogId(id,pageNum);
            blog.setTags(tags);
            blog.setComments(pageInfo.getList());
            box.setBlogVo(blogVo);
            box.setComments(pageInfo.getList());
            box.setTags(tags);
            box.setViews(blog.getViews());
            String s = JSONObject.toJSONString(box);
            JSONObject jsonObject = JSONObject.parseObject(s);
            Map<String, String> params = JSONObject.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
            });
            String st=MarkdownUtils.markdownToHtmlExtensions(blog.getContent());
            blog.setContent(st);
            redisTemplate.opsForHash().putAll(id,params);
        }else {
            String tags = (String)entries.get("tags");
            String comments = (String)entries.get("comments");
            String blogVo = (String)entries.get("blogVo");
            String views = (String) entries.get("views");
            String tagsrep = tags.replace("\\", "");
            String commentsrep = comments.replace("\\", "");
//            String blogVo1 = blogVo.replace("\\", "");
            List<Tag> tag = JSON.parseArray(tagsrep, Tag.class);
            List<Comment> comment = JSON.parseArray(commentsrep, Comment.class);
            BlogVo blogVo2 = JSON.parseObject(blogVo, BlogVo.class);
            blog=new Blog();
            BeanUtils.copyProperties(blogVo2,blog);
            blog.setTags(tag);
            blog.setComments(comment);
            Integer integer = Integer.valueOf(views);
            int res= integer+1;
            blog.setViews(res);
            String s = MarkdownUtils.markdownToHtmlExtensions(blog.getContent());
            blog.setContent(s);
            redisTemplate.opsForHash().put(id,"views",String.valueOf(res));

//            String list = (String)entries.get();
//            String replace = list.replace("\\", "");
//            List<Blog> blogs = JSON.parseArray(replace, Blog.class);
//            pageInfo =new PageInfo(blogs);
        }
            model.addAttribute("type",typeService.getType(blog.getTypeId()));
            model.addAttribute("blog", blog);
            model.addAttribute("tags", blog.getTags());
            return "blog";
        }

    @PostMapping("/search")
    public String search(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, String query, HttpServletRequest req){
        PageInfo<FirstPageBlog> pageInfo = blogService.getAllFirstPageBlog(pageNum, query);
        model.addAttribute("pageInfo",pageInfo);
        req.getSession().setAttribute("query",query);
        model.addAttribute("query",query);
        return "search";
    }
    @GetMapping("/search")
    public String searchFenYe(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,HttpServletRequest req){
        String query = (String) req.getSession().getAttribute("query");
        if (query==null){
            PageInfo<FirstPageBlog> pageInfo = blogService.getAllFirstPageBlog(pageNum, "");
            model.addAttribute("pageInfo",pageInfo);
            return "search";
        }
        PageInfo<FirstPageBlog> pageInfo = blogService.getAllFirstPageBlog(pageNum, query);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("query",query);
        return "search";
    }
    @GetMapping("/timeline")
    public String timeline(Model model){
        List<BlogQuery> allBlog = blogService.getAllBlog();
        model.addAttribute("blogs",allBlog);
        return "archives";
    }
    @GetMapping("/admin")
    public String admin(Model model,HttpServletRequest req){
        User user = (User)req.getSession().getAttribute("user");
        if (user==null){
            return "admin/login";
        }{
            return "admin/index";
        }
    }
    @GetMapping("/about")
    public String about(Model model,HttpServletRequest req){
        return "about";


    }
    @GetMapping("/gettypes")
    @ResponseBody
    public List<TypeVo> getTypes(Model model){
        List<Type> allType = typeService.getAllType();
        List<TypeVo> list = new ArrayList<>();
        for (Type type :allType){
            int size = type.getBlogs().size();
            TypeVo typeVo = new TypeVo(size,type);
            list.add(typeVo);
        }
        list.sort(new Comparator<TypeVo>() {
            @Override
            public int compare(TypeVo o1, TypeVo o2) {
                return o2.getSize()-o1.getSize();
            }
        });
        List<TypeVo> typeVos = list.subList(0, 4);
        return typeVos;

    }

    @GetMapping("/type")
    public String goType(Model model,@RequestParam Long id){
        PageInfo blogsByTypeId = blogService.getBlogsByTypeId(1, id);
        List<Type> allType = typeService.getAllType();
        model.addAttribute("types",allType);
        model.addAttribute("pageInfo",blogsByTypeId);
        model.addAttribute("id",id);
        return  "type";
    }
    @GetMapping("/sub")
    public String sub(Model model){
        return "sub";

    }
}
