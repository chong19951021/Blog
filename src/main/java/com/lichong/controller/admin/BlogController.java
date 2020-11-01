package com.lichong.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.entity.Blog;
import com.lichong.entity.Type;
import com.lichong.entity.User;
import com.lichong.queryvo.BlogQuery;
import com.lichong.queryvo.SearchBlog;
import com.lichong.queryvo.ShowBlog;
import com.lichong.service.BlogService;
import com.lichong.service.CommentService;
import com.lichong.service.TagService;
import com.lichong.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.index.PathBasedRedisIndexDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
@Controller
@RequestMapping("/admin")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;

    //博客列表
    @RequestMapping("/blogs")
    public String blogs(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        //按照排序字段 倒序 排序
        String orderBy = "update_time desc";
        PageHelper.startPage(pageNum,10,orderBy);
        List<BlogQuery> list = blogService.getAllBlog();
        PageInfo<BlogQuery> pageInfo = new PageInfo<BlogQuery>(list);
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("pageInfo",pageInfo);
        return "admin/blogs";
    }

    //跳转博客新增页面
    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("tags",tagService.getAllTag());
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    //博客新增
    @PostMapping("/blogs")
    public String post(@RequestParam String gaobu, Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        //设置blog的type
        blog.setType(typeService.getType(blog.getTypeId()));
        //设置blog中typeId属性
        blog.setTypeId(blog.getType().getId());
        // 设置blog中的tags属性
        String tagIds = blog.getTagIds();
        blog.setTags(tagService.listTag(tagIds));
        //设置用户id
        blog.setUserId(blog.getUser().getId());
        int b = blogService.saveBlog(blog,gaobu);
        String[] split = tagIds.split(",");
        blogService.saveMidTable(split,blog);

        if(b == 0){
            attributes.addFlashAttribute("message", "新增失败");
        }else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/blogs";
    }

    //删除文章
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/blogs";
    }

    //    跳转编辑修改文章
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        Blog blogById = blogService.getBlogById(id);
        List<Type> allType = typeService.getAllType();
        model.addAttribute("blog", blogById);
        model.addAttribute("tags", tagService.getAllTag());
        model.addAttribute("types", allType);
//        blogById.init();
        return "admin/blogs-input";
    }

//    //    编辑修改文章
//    @PostMapping("/blogs/{id}")
//    public String editPost( ShowBlog showBlog, RedirectAttributes attributes) {
//        int b = blogService.updateBlog();
//        if(b ==0){
//            attributes.addFlashAttribute("message", "修改失败");
//        }else {
//            attributes.addFlashAttribute("message", "修改成功");
//        }
//        return "redirect:/admin/blogs";
//    }

    //    搜索博客
    @PostMapping("/blogs/search")
    public String search(SearchBlog searchBlog, Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum) {
        PageInfo blogBySearch = blogService.getBlogBySearch(searchBlog,pageNum);
        model.addAttribute("pageInfo", blogBySearch);
        return "admin/blogs :: blogList";
    }
}
