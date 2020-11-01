package com.lichong.dao;

import com.lichong.entity.CloudVo;
import com.lichong.entity.Tag;
import com.lichong.entity.Type;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TagDao {

    int saveTag(Tag tag);
    Tag getTag(Long id);
    List<Tag> getAllTag();
    Tag getTagByName(String name);
    int updateTag(Tag tag);
    void deleteTag(Long id);
    List<Tag> listTag(List<Long> list);
    List<CloudVo> findVo();


}