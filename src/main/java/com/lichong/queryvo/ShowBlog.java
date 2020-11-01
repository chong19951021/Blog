package com.lichong.queryvo;

import com.lichong.entity.Tag;
import com.lichong.entity.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 编辑修改文章实体类
 * @Author: ONESTAR
 * @Date: Created in 23:41 2020/4/1
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
public class ShowBlog {

    private Long id;
    private String flag;
    private String title;
    private String content;
    private Long typeId;
    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private String firstPicture;
    private String description;
    private boolean recommend;
    private boolean published;
    private boolean shareStatement;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    private boolean appreciation;
    private boolean commentabled;
    private List<Tag> tags = new ArrayList<>();
    private String tagIds;

    private Date updateTime;
    public  void  init(){
        this.tagIds=tagsTools(this.getTags());
    }
    private String tagsTools(List<Tag> tags){
        if (!tags.isEmpty()){
            StringBuffer bu = new StringBuffer();
            boolean flag =false;
            for(Tag tag :tags){
                if (flag){
                    bu.append(",");
                }else {
                    flag=true;
                }
                bu.append(tag.getId());
            }
            return bu.toString();
        }else {
            return tagIds;
        }

    }

    public ShowBlog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }


    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isShareStatement() {
        return shareStatement;
    }

    public void setShareStatement(boolean shareStatement) {
        this.shareStatement = shareStatement;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isCommentabled() {
        return commentabled;
    }

    public void setCommentabled(boolean commentabled) {
        this.commentabled = commentabled;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ShowBlog{" +
                "id=" + id +
                ", flag='" + flag + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", typeId=" + typeId +
                ", firstPicture='" + firstPicture + '\'' +
                ", description='" + description + '\'' +
                ", recommend=" + recommend +
                ", published=" + published +
                ", shareStatement=" + shareStatement +
                ", appreciation=" + appreciation +
                ", commentabled=" + commentabled +
                ", updateTime=" + updateTime +
                '}';
    }
}