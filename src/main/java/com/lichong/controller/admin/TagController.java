package com.lichong.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.entity.Tag;
import com.lichong.entity.Type;
import com.lichong.service.TagService;
import com.lichong.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    //    分页查询分类列表
    @GetMapping("/tags")
    public String list(Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        //按照排序字段 倒序 排序
        String orderBy = "id asc";
        PageHelper.startPage(pageNum,5,orderBy);
        List<Tag> list = tagService.getAllTag();
        PageInfo<Tag> pageInfo = new PageInfo<Tag>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "admin/tags";
    }

//
    //  新增标签
    @PostMapping("/tags")
    public String post( Tag tag, RedirectAttributes attributes) {
        if (tag==null){
            attributes.addFlashAttribute("message", "不能为空！");
            return "redirect:/admin/tags";
        }
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            attributes.addFlashAttribute("message", "不能添加重复的标签");
            return "redirect:/admin/tags";
        }
        int t = tagService.saveTag(tag);
        if (t == 0) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    //    编辑修改分类
    @PostMapping("/tagsEdit")
    public String editPost(Tag tag, Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            attributes.addFlashAttribute("message", "此名称已经被占用");
            return "redirect:/admin/tags";
        }
        int t = tagService.updateTag(tag);
        if (t == 0 ) {
            attributes.addFlashAttribute("message", "编辑失败");
        } else {
            attributes.addFlashAttribute("message", "编辑成功");
        }
        return "redirect:/admin/tags";
    }

    //    删除分类
    @GetMapping("/tagsDelete")
    public String delete( Long id,RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}