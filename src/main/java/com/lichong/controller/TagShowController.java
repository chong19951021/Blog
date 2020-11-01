package com.lichong.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.entity.*;
import com.lichong.service.BlogService;
import com.lichong.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    //跳转到标签详情页
    @GetMapping("/taglist")
    public String Tag(Model model){

        List<Tag> allTag = tagService.getAllTag();
        model.addAttribute("tags",allTag);
        return "tag";
    }
    @PostMapping("/gotag")
    public String goTag(@RequestParam Long id,Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageInfo pageInfo = blogService.getBlogByTagId(pageNum,id);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("id",id);
        return  "tag :: bloglist";
    }
    @GetMapping("/gotag1")
    public String goTag1(@RequestParam Long id,Model model,
                          @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageInfo pageInfo = blogService.getBlogByTagId(pageNum,id);
        List<Tag> allTag = tagService.getAllTag();
        model.addAttribute("tags",allTag);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("id",id);
        return  "tag";
    }
    @PostMapping("/tagselect")
    public String tagselect(@RequestParam int id,Model model){
        model.addAttribute("id",id);
        List<Tag> allTag = tagService.getAllTag();
        model.addAttribute("tags",allTag);
        return "tag :: made";

    }
    @GetMapping("/get")
    @ResponseBody
    public RadarVo getValue(){
        ArrayList list = new ArrayList<>();
        ArrayList list2 = new ArrayList<>();
        List<Tag> allTag = tagService.getAllTag();
        for (Tag Tag :allTag){
            Radar radar = new Radar(Tag.getName(),"20");
            list.add(radar);
            list2.add(Tag.getBlogs().size());
        }
        RadarVo radarVo = new RadarVo(list,list2);
        return radarVo;
    }
    @GetMapping("/getAllLabel")
    @ResponseBody
    public List getAllLabel(){
        List<Tag> allTag = tagService.getAllTag();
        List objects = new ArrayList<>();
        for (Tag tag : allTag){
            List<Blog> blogByTagId1 = blogService.getBlogByTagId1(tag.getId());
            tag.setBlogs(blogByTagId1);
            CloudVo cloudVo = new CloudVo(tag.getId(),tag.getName(),tag.getBlogs().size());
            objects.add(cloudVo);
        }
        return objects;
    }
}
