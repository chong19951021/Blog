package com.lichong.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lichong.entity.Subscribe;
import com.lichong.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SubscribeController {
    @Autowired
    private SubscribeService subscribeService;
    @PostMapping("/sub")
    public String sub( Subscribe subscribe,RedirectAttributes attributes) {
        if (subscribe != null) {
            Subscribe exist = subscribeService.findExist(subscribe);
            if(exist!=null){
                attributes.addFlashAttribute("message", "不能重复订阅");
                return "redirect:/sub";
            }
            subscribeService.savaSubscribe(subscribe);
            attributes.addFlashAttribute("message", "成功发现宝藏，您将会在第一时间收到博客更新！");
            return "redirect:/sub";
        } else {
            attributes.addFlashAttribute("message", "你得写全啊！");
            return "redirect:/sub";
        }
    }
    @PostMapping("/subdel")
    public String subdel(@RequestParam String email,RedirectAttributes attributes){

        int i = subscribeService.delSubscribe(email);
        if (i==0){
            attributes.addFlashAttribute("message", "删除失败，给他打电话吧。Tel：17330548647");
            return "redirect:/sub";
        }
        attributes.addFlashAttribute("message", "你果然离开我了，拜拜！下一个更乖！");
        return "redirect:/sub";
    }
    @GetMapping("/subdelget")
    public String delGet(@RequestParam Long id, Model model,RedirectAttributes attributes){
        int i = subscribeService.delSubscribeById(id);
        if (i==0){
            attributes.addFlashAttribute("message", "删除失败");
            return "redirect:/sublist";
        }
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/sublist";
    }
    @GetMapping("/sublist")
    public String sublist( Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        String orderBy = "time desc";
        PageHelper.startPage(pageNum,10,orderBy);
        List<Subscribe> allEmails = subscribeService.findAllEmails();
        PageInfo<Subscribe> pageInfo = new PageInfo<>(allEmails);
        model.addAttribute("pageInfo",pageInfo);
        return "/admin/subs";
    }
    @GetMapping("/admin/sub")
    public String sublist( Model model){
        return "forward:/sublist";
    }
}
