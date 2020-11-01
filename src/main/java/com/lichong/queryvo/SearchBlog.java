package com.lichong.queryvo;

import io.netty.util.collection.ByteCollections;

/**
 * @Description: 搜索博客管理列表
 * @Author: ONESTAR
 * @Date: Created in 20:11 2020/4/2
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
public class SearchBlog {

    private String title;
    private Long typeId;

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    private Boolean recommend;

    public SearchBlog() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }


    @Override
    public String toString() {
        return "SearchBlog{" +
                "title='" + title + '\'' +
                ", typeId=" + typeId +
                ", recommend=" + recommend +
                '}';
    }
}