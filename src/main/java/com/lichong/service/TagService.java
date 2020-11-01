package com.lichong.service;

import com.lichong.entity.CloudVo;
import com.lichong.entity.Tag;

import java.util.List;

public interface TagService {

    int saveTag(Tag tag);
    Tag getTag(Long id);
    List<Tag> getAllTag();
    List<Tag> listTag(String ids);
    Tag getTagByName(String name);
    int updateTag(Tag tag);
    void deleteTag(Long id);
    List<CloudVo> findVo();




}