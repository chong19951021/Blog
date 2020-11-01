package com.lichong.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.entity.Blog;
import com.lichong.entity.Radar;
import com.lichong.entity.RadarVo;
import com.lichong.entity.Type;
import com.lichong.service.BlogService;
import com.lichong.service.TypeService;
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
public class TypeShowController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    //跳转到分类详情页
    @GetMapping("/typelist")
    public String type(Model model){

        List<Type> allType = typeService.getAllType();
        model.addAttribute("types",allType);
        return "type";
    }
    @PostMapping("/gotype")
    public String goType(@RequestParam Long id,Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageInfo pageInfo = blogService.getBlogsByTypeId(pageNum,id);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("id",id);
       return  "type :: bloglist";
    }
    @GetMapping("/gotype1")
    public String goType1(@RequestParam Long id,Model model,
                         @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        PageInfo pageInfo = blogService.getBlogsByTypeId(pageNum,id);
        List<Type> allType = typeService.getAllType();
        model.addAttribute("types",allType);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("id",id);
        return  "type";
    }
    @PostMapping("/typeselect")
    public String typeselect(@RequestParam int id,Model model){
        model.addAttribute("id",id);
        List<Type> allType = typeService.getAllType();
        model.addAttribute("types",allType);
        return "type :: made";

    }
    @GetMapping("/getvalue")
    @ResponseBody
    public RadarVo getValue(){
       ArrayList list = new ArrayList<>();
       ArrayList list2 = new ArrayList<>();
       List<Type> allType = typeService.getAllType();
       for (Type type :allType){
           Radar radar = new Radar(type.getName(),"20");
           list.add(radar);
           list2.add(type.getBlogs().size());
       }
        RadarVo radarVo = new RadarVo(list,list2);
       return radarVo;
    }
}
