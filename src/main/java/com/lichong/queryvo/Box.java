package com.lichong.queryvo;

import com.lichong.entity.Comment;
import com.lichong.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public class Box {
    private BlogVo blogVo;
    private int views;

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    private List<Comment> comments = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public BlogVo getBlogVo() {
        return blogVo;
    }

    public void setBlogVo(BlogVo blogVo) {
        this.blogVo = blogVo;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
