package com.lichong.service.impl;

import com.lichong.dao.TagDao;
import com.lichong.entity.CloudVo;
import com.lichong.entity.Tag;
import com.lichong.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public int saveTag(Tag tag) {
        return tagDao.saveTag(tag);
    }

    @Override
    public Tag getTag(Long id) {
        return tagDao.getTag(id);
    }

    @Override
    public List<Tag> getAllTag() {
        return tagDao.getAllTag();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagDao.listTag(convert(ids));
    }

    private List convert(String ids){
        List list =new ArrayList<>();
        if(!"".equals(ids)&&ids!=null){
            String[] split = ids.split(",");
            for (int i=0;i<split.length;i++){
                list.add(split[i]);
            }
        }
        return list;
    }

    @Override
    public Tag getTagByName(String name) {
        return tagDao.getTagByName(name);
    }

    @Override
    public int updateTag(Tag tag) {
        return tagDao.updateTag(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tagDao.deleteTag(id);
    }

    @Override
    public List<CloudVo> findVo() {
        return tagDao.findVo();
    }
}
